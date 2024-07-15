package kr.kro.dokbaro.server.fixture

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder

class FixtureBuilder {
	companion object {
		val fixtureMonkeyBuilder: FixtureMonkey =
			FixtureMonkey
				.builder()
				.plugin(KotlinPlugin())
				.build()

		inline fun <reified T> give() = fixtureMonkeyBuilder.giveMeBuilder<T>()
	}
}