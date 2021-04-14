### Json deserializer 
## in pure kotlin
##  [based on Eclipse JSON Parser for Java](https://eclipsesource.com/blogs/2013/04/18/minimal-json-parser-for-java)

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
        arrx = jList("arrx") {
            this.int() ?: -1
        }
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