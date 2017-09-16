package ua.kotlin.demo.config

object EmptyKotlinConfig : Config {
    override fun beanDefinitions(): List<BeanDefinition> = emptyList()
}

class KotlinConfig(private val beanDescription: Any) : Config {

    @Suppress("UNCHECKED_CAST")
    override fun beanDefinitions(): List<BeanDefinition> = when (beanDescription) {
        is List<*> -> beanDescription.map {
            val (name, properties) = it as Pair<String, Any>
            when (properties) {
                is Class<*> -> BeanDefinition(name, properties)
                is List<*> -> beanDefinitionFromProperties(name, properties as List<Pair<String, Any>>)
                else -> throw IllegalArgumentException()
            }
        }
        else -> emptyList()
    }

    private fun beanDefinitionFromProperties(name: String,  properties: List<Pair<String, Any>>) : BeanDefinition {
        val type = properties.getProperty("type") as Class<*>
        val isPrototype = (properties.getProperty("isPrototype")?: false) as Boolean
        return BeanDefinition(name, type, isPrototype)
    }

    private fun List<Pair<String, Any?>>.getProperty(name: String) = find { it.first == name }?.second

}


