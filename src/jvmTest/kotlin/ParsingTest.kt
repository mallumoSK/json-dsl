import org.junit.jupiter.api.Test
import tk.mallumo.json.dsl.JsonDsl
import tk.mallumo.log.LOGGER_CONSOLE_FORCE
import tk.mallumo.log.LOGGER_IS_ENABLED
import tk.mallumo.log.log
import tk.mallumo.log.logWithOffset
import java.io.File
import kotlin.system.measureTimeMillis


class ItemHolder(
    val item: Int,
    val str: String,
    val arrx: List<Int>?,
)

class ParsingTest {

    init {
        LOGGER_CONSOLE_FORCE = true
        LOGGER_IS_ENABLED = true
    }

    @Test
    fun simpleObject() {
        val jsonObject = """
        {
            "item":1,
            "str":1,
            "arrx":[1,2,3,4]
        }
    """

        val resp: ItemHolder = JsonDsl.decodeObject(jsonObject) {
            ItemHolder(
                item = this["item"] ?: -22,
                str = this["str"] ?: "",
                arrx = jList("arrx") {
                    this.int() ?: 555
                }
            )
        }
        assert(resp.item == 1)
        assert(resp.str == "1")
        assert(resp.arrx == listOf(1, 2, 3, 4))
    }

    @Test
    fun simpleList() {
        val jsonList = """
        [1,2,3,4]
    """
        val resp = JsonDsl.decodeList<Int?>(jsonList) {
            int()
        }
        assert(resp == listOf(1, 2, 3, 4))
    }

    @Test
    fun listOfObjects() {

        val jsonListOfObjects = """
        [
        { "item":0 },
        { "item":1 }
        ]
    """


        val resp = JsonDsl.decodeList<ItemHolder>(jsonListOfObjects) {
            jObj {
                ItemHolder(
                    item = this["item"] ?: -1,
                    str = "",
                    arrx = listOf()
                )
            }
        }

        assert(resp[0].item == 0)
        assert(resp[1].item == 1)
        assert(resp.size == 2)
    }

    @Test
    fun multiTestDSL() {
        testDSL(1_0000)
    }

    @Test
    fun singleTestDSL() {
        testDSL(1)
    }

    private fun testDSL(repeat: Int = 0) {
        val json = File(javaClass.classLoader.getResource("issue.json")!!.file).readText()
        val time = measureTimeMillis {
            repeat(repeat) {
                decodeByDsl(json)
                    .takeIf { repeat == 1 }
                    ?.also(::log)
            }
        }
        logWithOffset(
            """
                
        TimeSpend:  ${time / repeat.toFloat()}
        Iterations: $repeat
        
    """.trimIndent(),
            1
        )
    }
}

private fun decodeByDsl(input: String): List<JIRA_ISSUE> {
    return JsonDsl.decodeObject(input) {
        jList("issues") {
            jObj {
                JIRA_ISSUE().apply {
                    id = this@jObj["id"] ?: id
                    key = this@jObj["key"] ?: key

                    jObj<Unit>("fields") {

                        summary = this["summary"] ?: summary
                        description = this["description"] ?: description
                        created = this["created"] ?: created

                        labels = jList<String>("labels") {
                            string() ?: ""
                        }.joinToString()

                        components = jList<String>("components") {
                            jObj {
                                this["name"] ?: ""
                            }
                        }.joinToString()

                        jObj<Unit>("priority") {
                            priority_id = this["id"] ?: priority_id
                            priority_name = this["name"] ?: priority_name
                        }

                        jObj<Unit>("status") {
                            status_id = this["id"] ?: status_id
                            status_name = this["name"] ?: status_name
                        }

                        jObj<Unit>("issuetype") {
                            type_name = this["name"] ?: type_name
                            type_subtask = if (this["subtask"] ?: false) 1 else 0
                        }

                        jObj<Unit>("assignee") {
                            assignee_id = this["name"] ?: assignee_id
                            assignee_key = this["key"] ?: assignee_key
                            assignee_name = this["displayName"] ?: assignee_name
                            assignee_active = if (this["active"] ?: false) 1 else 0
                        }

                        jObj<Unit>("reporter") {
                            reporter_id = this["name"] ?: reporter_id
                            reporter_key = this["key"] ?: reporter_key
                            reporter_name = this["displayName"] ?: reporter_name
                            reporter_active = if (this["active"] ?: false) 1 else 0
                        }

                        jObj<Unit>("creator") {
                            creator_id = this["name"] ?: creator_id
                            creator_key = this["key"] ?: creator_key
                            creator_name = this["displayName"] ?: creator_name
                            creator_active = if (this["active"] ?: false) 1 else 0
                        }

                        jObj<Unit>("project") {
                            project_id = this["id"] ?: project_id
                            project_key = this["key"] ?: project_key
                            project_name = this["name"] ?: project_name
                            project_type = this["projectTypeKey"] ?: project_type

                            jObj<Unit>("projectCategory") {
                                project_cat_name = this["name"] ?: project_cat_name
                                project_cat_desc = this["description"] ?: project_cat_desc
                            }
                        }
                    }
                }
            }
        }
    }
}

open class JIRA_ISSUE(
    var id: Long = -1,
    var key: String = "",
    var summary: String = "",
    var description: String = "",
    var created: String = "",

    var labels: String = "",
    var components: String = "",

    var type_name: String = "",
    var type_subtask: Int = 0,

    var assignee_id: String = "",
    var assignee_key: String = "",
    var assignee_name: String = "",
    var assignee_active: Int = 0,

    var reporter_id: String = "",
    var reporter_key: String = "",
    var reporter_name: String = "",
    var reporter_active: Int = 0,

    var creator_id: String = "",
    var creator_key: String = "",
    var creator_name: String = "",
    var creator_active: Int = 0,

    var priority_id: Long = 0,
    var priority_name: String = "",

    var status_id: Long = -1,
    var status_name: String = "",

    var project_id: Long = -1,
    var project_key: String = "",
    var project_name: String = "",
    var project_type: String = "",
    var project_cat_name: String = "",
    var project_cat_desc: String = "",
)

