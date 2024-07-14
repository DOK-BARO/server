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
		path: Path,
		body: Any? = null,
		param: MultiValueMap<String, String>? = null,
	): ResultActions {
		val requestBuilder =
			post(path.endPoint, *path.pathVariable)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.apply {
					body?.let { content(toBody(it)) }
				}.apply {
					param?.let { params(it) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performPut(
		path: Path,
		body: Any? = null,
		param: MultiValueMap<String, String>? = null,
	): ResultActions {
		val requestBuilder =
			put(path.endPoint, path.pathVariable)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.apply {
					body?.let { content(toBody(it)) }
				}.apply {
					param?.let { params(it) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performPatch(
		path: Path,
		body: Any? = null,
		param: MultiValueMap<String, String>? = null,
	): ResultActions {
		val requestBuilder =
			patch(path.endPoint, path.pathVariable)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.apply {
					body?.let { content(toBody(it)) }
				}.apply {
					param?.let { params(it) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performDelete(
		path: Path,
		body: Any? = null,
		param: MultiValueMap<String, String>? = null,
	): ResultActions {
		val requestBuilder =
			delete(path.endPoint, path.pathVariable)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.apply {
					param?.let { params(it) }
				}.apply {
					body?.let { content(toBody(it)) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performGet(
		path: Path,
		param: MultiValueMap<String, String>? = null,
	): ResultActions =
		mockMvc.perform(
			get(path.endPoint, path.pathVariable)
				.apply {
					param?.let { params(it) }
				},
		)

	protected fun performFormData(
		method: HttpMethod,
		path: Path,
		requestPartKey: String,
	): ResultActions =
		mockMvc.perform(
			multipart(method, path.endPoint, path.pathVariable)
				.file(requestPartKey, ByteArray(0))
				.with(csrf())
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.characterEncoding(StandardCharsets.UTF_8),
		)

	private fun toBody(body: Any) = mapper.writeValueAsString(body)

	protected fun print(
		title: String,
		vararg snippets: Snippet,
	) = document(title, getDocumentRequest(), getDocumentResponse(), *snippets)

	private fun getDocumentRequest() = preprocessRequest(prettyPrint())

	private fun getDocumentResponse() = preprocessResponse(prettyPrint())
}