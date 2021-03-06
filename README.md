### Json deserializer 
## in pure kotlin v1.4.32
##  [based on Eclipse JSON Parser for Java](https://eclipsesource.com/blogs/2013/04/18/minimal-json-parser-for-java)

Speed is not great but enough for parsing json by specification, dynamically changing in time.  

##### Throw exception only if
* json syntax error
* cast between primitives is in some cases impossible

##### Library is currently generated for
* jvm
* js
* linux64

If you need another platform,
no problem, 
let me known :)


![https://mallumo.jfrog.io/artifactory/gradle-dev-local/tk/mallumo/json-dsl/](https://img.shields.io/maven-metadata/v?color=%234caf50&metadataUrl=https%3A%2F%2Fmallumo.jfrog.io%2Fartifactory%2Fgradle-dev-local%2Ftk%2Fmallumo%2Fjson-dsl%2Fmaven-metadata.xml&style=for-the-badge "Version")

```groovy
dependencies {
    implementation("tk.mallumo:json-dsl:+")
}
```
```groovy
//build.gradle
repositories {
    maven {
        url = uri("https://mallumo.jfrog.io/artifactory/gradle-dev-local")
    }
}
```
```groovy
//build.gradle.kts
repositories {
     maven("https://mallumo.jfrog.io/artifactory/gradle-dev-local")
}
```

**core logic + preview is in test cases**

### simple preview:

```kotlin
// result object
class ItemHolder(
    val item: Int,
    val str: String,
    val arrx: List<Int>?,
)
```

### object
```kotlin
val jsonObject = """
{
    "item":1,
    "str":1,
    "arrx":[1,2,3,4]
}
"""
val resp: ItemHolder = JsonDsl.decodeObject(jsonObject) {
    ItemHolder(
        item = this["item"] ?: -1,
        str = this["str"] ?: "",
        arrx = jList("arrx") { int() ?: -1 }
    )
}
```

### array
```kotlin
val jsonList = """
[1,2,3,4]
"""
val resp:List<Int?> = JsonDsl.decodeList(jsonList) { int() }
```

### array of objects
```kotlin
val jsonListOfObjects = """
[
    { "item":0 },
    { "item":1 }
]
"""

val resp:List<ItemHolder> = JsonDsl.decodeList(jsonListOfObjects) {
    jObj {
         ItemHolder(
            item = this["item"] ?: -1,
            str = "",
            arrx = listOf()
         )
    }
}
```