package ua.kotlin.demo.context

interface Context {

    fun getBeanDefinitionNames(): List<String>

    fun <T> getBean(beanName: String): T

}