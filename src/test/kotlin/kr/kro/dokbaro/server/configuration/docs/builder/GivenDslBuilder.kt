package kr.kro.dokbaro.server.configuration.docs.builder

import org.springframework.http.MediaType
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

data class GivenDslBuilder(
	var body: Any? = null,
	var headers: StringPairDslBuilder = StringPairDslBuilder(),
	val parameters: StringPairDslBuilder = StringPairDslBuilder(),
	val cookies: StringPairDslBuilder = StringPairDslBuilder(),
	var contentType: MediaType = MediaType.APPLICATION_JSON,
	var charset: Charset = StandardCharsets.UTF_8,
) {
	fun body(block: () -> Any?): GivenDslBuilder {
		body = block.invoke()
		return this
	}

	fun headers(block: StringPairDslBuilder.() -> Unit): GivenDslBuilder = this

	fun parameters(block: StringPairDslBuilder.() -> Unit): GivenDslBuilder = this

	fun cookies(block: StringPairDslBuilder.() -> Unit): GivenDslBuilder = this

	fun contentType(block: () -> MediaType): GivenDslBuilder {
		contentType = block.invoke()
		return this
	}

	fun charset(block: () -> Charset): GivenDslBuilder {
		charset = block.invoke()
		return this
	}
}