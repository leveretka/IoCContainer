package ua.kotlin.demo.context

import org.junit.Assert.*
import org.junit.Test
import ua.kotlin.demo.config.EmptyKotlinConfig
import ua.kotlin.demo.config.KotlinConfig

class ApplicationContextTest {

    private lateinit var context: Context

    @Test
    fun createApplicationContext() {
        context = emptyContext()
        assertNotNull(context)
    }

    @Test
    fun beanDefinitionsWithoutConfig() {
        context = emptyContext()
        val beanDefinitionNames = context.getBeanDefinitionNames()
        assertTrue(beanDefinitionNames.isEmpty())
    }

    @Test(expected = NoBeanFoundException::class)
    fun getBeanWithoutConfig() {
        context = emptyContext()
        val bean = context.getBean<Any>(beanName = "AnyBeanName")
        assertNull(bean)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getBeanWithEmptyBeanName() {
        context = emptyContext()
        context.getBean<Any>(beanName = "")
    }


    @Test
    fun beanDefinitionsWithEmptyConfig() {
        context = ApplicationContext(EmptyKotlinConfig)
        val beanDefinitionNames = context.getBeanDefinitionNames()
        assertTrue(beanDefinitionNames.isEmpty())
    }

    @Test
    fun beanDefinitionsWithOneBeanInKotlinConfig() {
        val beanDescription = listWithSimpleBeanDescription()
        context = ApplicationContext(KotlinConfig(beanDescription))
        val beanDefinitionNames = context.getBeanDefinitionNames()
        assertTrue(beanDefinitionNames.isNotEmpty())
    }

    @Test
    fun beanDefinitionsWithOneNamedBeanInKotlinConfig() {
        val beanDescription = listWithOneBeanDescription()
        context = ApplicationContext(KotlinConfig(beanDescription))
        val beanDefinitionNames = context.getBeanDefinitionNames()
        assertEquals(listOf("beanName"), beanDefinitionNames)
    }

    @Test
    fun beanDefinitionsWithSeveralNamedBeanInKotlinConfig() {
        val beanDescription = listWithTwoBeanDescription()
        context = ApplicationContext(KotlinConfig(beanDescription))
        val beanDefinitionNames = context.getBeanDefinitionNames()
        assertEquals(listOf("bean1", "bean2"), beanDefinitionNames)
    }

    @Test(expected = IllegalArgumentException::class)
    fun beanDefinitionsWithSeveralSameNamedBeansInKotlinConfig() {
        val beanDescription = listWithTwoEqualBeanDefinition()
        context = ApplicationContext(KotlinConfig(beanDescription))
    }

    @Test(expected = NoBeanFoundException::class)
    fun getBeanWithNoBeanInConfig() {
        val beanDescription = listWithTwoBeanDescription()
        context = ApplicationContext(KotlinConfig(beanDescription))
        context.getBean<Any>("bean")
    }


    @Test
    fun getExistingBeanFromConfig() {
        val beanDescription = listWithSingleBeanDescription()
        context = ApplicationContext(KotlinConfig(beanDescription))
        val bean = context.getBean<TestBean>("bean")
        assertNotNull(bean)
    }

    @Test
    fun getSeveralExistingBeanFromConfig() {
        val beanDescription = listWithTwoDifferentBeans()
        context = ApplicationContext(KotlinConfig(beanDescription))


        val bean1 = context.getBean<TestBean>("bean1")
        assertNotNull(bean1)

        val bean2 = context.getBean<Any>("bean2")
        assertNotNull(bean2)
    }

    @Test
    fun beanShouldBeSingleton() {
        val beanDescription = listWithSingleBeanDescription()
        context = ApplicationContext(KotlinConfig(beanDescription))
        val bean1 = context.getBean<TestBean>("bean")
        val bean2 = context.getBean<TestBean>("bean")
        assertSame(bean1, bean2)
    }

    @Test
    fun beanShouldBePrototype() {
        val beanDescription = listWithSinglePrototypeBeanDescription()
        context = ApplicationContext(KotlinConfig(beanDescription))
        val bean1 = context.getBean<TestBean>("bean")
        val bean2 = context.getBean<TestBean>("bean")
        assertNotSame(bean1, bean2)
    }

    private fun listWithSingleBeanDescription() = listOf("bean" to TestBean::class.java)
    private fun listWithSinglePrototypeBeanDescription() = listOf("bean" to
            listOf("type" to TestBean::class.java, "isPrototype" to true))
    private fun listWithSimpleBeanDescription() = listOf("" to Any::class.java)
    private fun listWithOneBeanDescription() = listOf("beanName" to Any::class.java)
    private fun listWithTwoBeanDescription() = listOf("bean1" to Any::class.java, "bean2" to Any::class.java)
    private fun listWithTwoEqualBeanDefinition() = listOf("bean1" to Any::class.java, "bean1" to Any::class.java)
    private fun listWithTwoDifferentBeans() = listOf("bean1" to TestBean::class.java, "bean2" to Any::class.java)

    private fun emptyContext() = ApplicationContext()
}

private class TestBean
