package ua.kotlin.demo.config

interface Config {

    fun beanDefinitions(): List<BeanDefinition>
}

data class BeanDefinition(val beanName: String, val beanType: Class<*>, val isPrototype: Boolean = false)
