package ua.kotlin.demo.context

import ua.kotlin.demo.config.BeanDefinition
import ua.kotlin.demo.config.Config

class ApplicationContext (private val config: Config? = null) : Context {

    private val beanDefinitions = mutableMapOf<String, Any?>()

    init {
        config?.beanDefinitions()?.checkDuplicates()
    }

    override fun getBeanDefinitionNames(): List<String> = config?.beanDefinitions()?.
            map { it.beanName}?.
            toList() ?: emptyList()

    override fun <T> getBean(beanName: String) = getBeanByName<T>(beanName)?: createNewBean(beanName)

    @Suppress("UNCHECKED_CAST")
    private fun <T> createNewBean(beanName: String): T {
        val beanDefinition = findBeanDefinition(beanName)!!
        val bean = beanDefinition.beanType.newInstance()
        if (!beanDefinition.isPrototype)
            beanDefinitions.put(beanName, bean)
        return bean as T
    }

    private fun findBeanDefinition(beanName: String) = config?.beanDefinitions()?.find { it.beanName == beanName }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getBeanByName(beanName: String): T {
        if (beanName.isBlank())
            throw IllegalArgumentException()

        if (!getBeanDefinitionNames().contains(beanName))
            throw NoBeanFoundException()

        return beanDefinitions[beanName] as T
    }

    private fun List<BeanDefinition>.checkDuplicates() {
        if (size != distinct().size)
            throw IllegalArgumentException("Ambiguous bean names in config!")
    }
}
