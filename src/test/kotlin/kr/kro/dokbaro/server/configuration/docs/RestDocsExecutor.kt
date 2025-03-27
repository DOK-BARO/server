package kr.kro.dokbaro.server.configuration.docs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.servlet.http.Cookie
import kr.kro.dokbaro.server.configuration.docs.builder.GivenDslBuilder
import kr.kro.dokbaro.server.configuration.docs.builder.ThenDslBuilder
import org.springframework.http.HttpMethod
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.restdocs.snippet.Snippet
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.util.MultiValueMap

class RestDocsExecutor(
	private val mvc: MockMvc,
	private val method: HttpMethod,
	private val path: Path,
	private val givenBuilder: GivenDslBuilder = GivenDslBuilder(),
	private val thenBuilder: ThenDslBuilder = ThenDslBuilder(),
	block: RestDocsExecutor.() -> Unit,
) {
	init {
		this.apply(block)
		execute()
	}

	fun given(block: GivenDslBuilder.() -> Unit) {
		givenBuilder.apply(block)
	}

	fun then(block: ThenDslBuilder.() -> Unit) {
		thenBuilder.apply(block)
	}

	private fun execute() {
		val action =
			RestDocumentationRequestBuilders
				.request(method, path.endPoint, *path.pathVariable)
				.with(csrf())
				.contentType(givenBuilder.contentType)
				.characterEncoding(givenBuilder.charset)
				.apply {
					if (givenBuilder.headers.exists()) {
						givenBuilder.headers.map.forEach { (k, v) ->
							header(k, v)
						}
					}
				}.apply {
					if (givenBuilder.parameters.exists()) {
						params(givenBuilder.parameters.map)
					}
				}.apply {
					if (givenBuilder.cookies.exists()) {
						cookie(*mapToCookies(givenBuilder.cookies.map))
					}
				}.apply {
					givenBuilder.body?.let {
						content(
							objectMapper().writeValueAsString(it),
						)
					}
				}

		mvc
			.perform(action)
			.apply { thenBuilder.expect?.let { andExpect(it) } }
			.andDo(
				document(
					thenBuilder.docsTitle,
					getDocumentRequest(),
					getDocumentResponse(),
					*buildList<Snippet> {
						if (thenBuilder.pathParameters.exists()) {
							add(pathParameters(thenBuilder.pathParameters.toDescriptor()))
						}
						if (thenBuilder.queryParameters.exists()) {
							add(queryParameters(thenBuilder.queryParameters.toDescriptor()))
						}
						if (thenBuilder.requestFields.exists()) {
							add(requestFields(thenBuilder.requestFields.toDescriptor()))
						}
						if (thenBuilder.responseFields.exists()) {
							add(responseFields(thenBuilder.responseFields.toDescriptor()))
						}
					}.toTypedArray(),
				),
			)
	}

	private fun objectMapper(): ObjectMapper {
		val objectMapper = ObjectMapper()
		objectMapper.registerModule(JavaTimeModule())
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

		return objectMapper
	}

	private fun mapToCookies(cookieMap: MultiValueMap<String, String>): Array<Cookie> =
		cookieMap.map { (name, value) -> Cookie(name, value[0]) }.toTypedArray()

	private fun getDocumentRequest(): OperationRequestPreprocessor =
		preprocessRequest(
			prettyPrint(),
			modifyUris().host("dokbaro.com").removePort(),
		)

	private fun getDocumentResponse(): OperationResponsePreprocessor = preprocessResponse(prettyPrint())
}