package de.earley.kompanionDI.mocking

import de.earley.kompanionDI.Provider
import de.earley.kompanionDI.value

/* Flow is as follows:
* val foo = [some provider}
* foo.mock with [the mock]
*/

interface Mockable<T, P> {
	infix fun with(mock: Provider<T, P>): MockProvider<T, P>
	@Deprecated(replaceWith = ReplaceWith("withValue"), message = "Use to better named withValue", level = DeprecationLevel.HIDDEN)
	infix fun withBean(mock: T): MockProvider<T, P> = withValue(mock)
	infix fun withValue(mock: T): MockProvider<T, P>
}

private class Mock<T, P>(private val provider: Provider<T, P>) : Mockable<T, P> {
	override infix fun with(mock: Provider<T, P>): MockProvider<T, P> = MockProvider(provider, mock)
	override infix fun withValue(mock: T): MockProvider<T, P> = this with value(mock)
}

val <T, P> Provider<T, P>.mock
	get(): Mockable<T, P> = Mock(this)

fun <P> mocksOf(vararg mocks: MockProvider<*, P>): MockMap<P> = mutableMocksOf(*mocks)

fun <P> mutableMocksOf(vararg mocks: MockProvider<*, P>): MutableMockMap<P> = HashMockMap<P>().apply {
	mocks.forEach { add(it) }
}