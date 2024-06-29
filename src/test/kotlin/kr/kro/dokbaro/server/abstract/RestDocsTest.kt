package kr.kro.dokbaro.server.abstract

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import kr.kro.dokbaro.server.configuration.security.TestSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.snippet.Snippet
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.util.MultiValueMap
import java.nio.charset.StandardCharsets

@ContextConfiguration
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithUserDetails(value = "username", userDetailsServiceBeanName = "testUserDetailService")
@Import(TestSecurityConfig::class)
abstract class RestDocsTest : StringSpec() {
	override fun extensions() = listOf(SpringExtension)

	@Autowired
	private lateinit var mockMvc: MockMvc
	private val mapper = jacksonObjectMapper()

	protected fun performPost(
		endpoint: String,
		body: Any? = null,
	): ResultActions {
		val requestBuilder =
			post(endpoint)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.apply {
					body?.let { content(generateBody(it)) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performPut(
		endpoint: String,
		body: Any? = null,
	): ResultActions {
		val requestBuilder =
			put(endpoint)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.apply {
					body?.let { content(generateBody(it)) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performPatch(
		endpoint: String,
		body: Any? = null,
	): ResultActions {
		val requestBuilder =
			patch(endpoint)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.apply {
					body?.let { content(generateBody(it)) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performDelete(
		endpoint: String,
		body: Any? = null,
	): ResultActions {
		val requestBuilder =
			delete(endpoint)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.apply {
					body?.let { content(generateBody(it)) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performGet(
		endpoint: String,
		vararg urlValue: String,
	): ResultActions = mockMvc.perform(get(endpoint, *urlValue))

	protected fun performGet(
		endpoint: String,
		params: MultiValueMap<String, String>,
		vararg urlValue: String,
	): ResultActions = mockMvc.perform(get(endpoint, *urlValue).params(params))

	protected fun performFormData(
		method: HttpMethod,
		endpoint: String,
		requestPartKey: String,
	): ResultActions =
		mockMvc.perform(
			multipart(method, endpoint)
				.file(requestPartKey, ByteArray(0))
				.with(csrf())
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.characterEncoding(StandardCharsets.UTF_8),
		)

	private fun generateBody(obj: Any) = mapper.writeValueAsString(obj)

	protected fun print(
		title: String,
		vararg snippets: Snippet,
	) = document(title, getDocumentRequest(), getDocumentResponse(), *snippets)

	private fun getDocumentRequest() = preprocessRequest(prettyPrint())

	private fun getDocumentResponse() = preprocessResponse(prettyPrint())
}