package kr.kro.dokbaro.server.fixture

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder

object FixtureBuilder {
	val fixtureMonkeyBuilder: FixtureMonkey =
		FixtureMonkey
			.builder()
			.plugin(KotlinPlugin())
			.build()

	inline fun <reified T> give() = fixtureMonkeyBuilder.giveMeBuilder<T>()
}