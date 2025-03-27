package kr.kro.dokbaro.server.configuration.docs.builder

import org.springframework.test.web.servlet.ResultMatcher

data class ThenDslBuilder(
	var expect: ResultMatcher? = null,
	var docsTitle: String? = null,
	val pathParameters: ParameterDslBuilder = ParameterDslBuilder(),
	val queryParameters: ParameterDslBuilder = ParameterDslBuilder(),
	val requestFields: FieldDslBuilder = FieldDslBuilder(),
	val responseFields: FieldDslBuilder = FieldDslBuilder(),
) {
	fun expect(block: () -> ResultMatcher): ThenDslBuilder {
		expect = block.invoke()
		return this
	}

	fun docsTitle(block: () -> String): ThenDslBuilder {
		docsTitle = block.invoke()
		return this
	}

	fun pathParameters(block: ParameterDslBuilder.() -> Unit): ThenDslBuilder {
		pathParameters.apply(block)
		return this
	}

	fun queryParameters(block: ParameterDslBuilder.() -> Unit): ThenDslBuilder {
		queryParameters.apply(block)
		return this
	}

	fun requestFields(block: FieldDslBuilder.() -> Unit): ThenDslBuilder {
		requestFields.apply(block)
		return this
	}

	fun responseFields(block: FieldDslBuilder.() -> Unit): ThenDslBuilder {
		responseFields.apply(block)
		return this
	}
}