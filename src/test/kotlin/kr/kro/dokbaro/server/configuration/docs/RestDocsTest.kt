package kr.kro.dokbaro.server.configuration.docs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import jakarta.servlet.http.Cookie
import kr.kro.dokbaro.server.configuration.security.TestSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.snippet.Snippet
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.util.LinkedMultiValueMap
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

	protected fun performPost(
		path: Path,
		body: Any? = null,
		param: MultiValueMap<String, String>? = null,
		cookie: Map<String, String>? = null,
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
				}.apply {
					cookie?.let { cookie(*mapToCookies(it)) }
				}

		return mockMvc.perform(requestBuilder)
	}

	protected fun performPut(
		path: Path,
		body: Any? = null,
		param: MultiValueMap<String, String>? = null,
	): ResultActions {
		val requestBuilder =
			put(path.endPoint, *path.pathVariable)
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
			patch(path.endPoint, *path.pathVariable)
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
			delete(path.endPoint, *path.pathVariable)
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
		param: Map<String, String> = mapOf(),
	): ResultActions =
		mockMvc.perform(
			get(path.endPoint, *path.pathVariable)
				.apply {
					params(LinkedMultiValueMap(param.mapValues { listOf(it.value) }))
				},
		)

	protected fun performWithMultipart(
		path: Path,
		param: Map<String, String> = mapOf(),
		fileName: String = "file",
	): ResultActions =
		mockMvc.perform(
			multipart(path.endPoint, *path.pathVariable)
				.apply {
					params(LinkedMultiValueMap(param.mapValues { listOf(it.value) }))
				}.file(MockMultipartFile(fileName, byteArrayOf()))
				.with(csrf())
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.characterEncoding(StandardCharsets.UTF_8),
		)

	private fun toBody(body: Any) = objectMapper().writeValueAsString(body)

	private fun objectMapper(): ObjectMapper {
		val objectMapper = ObjectMapper()
		objectMapper.registerModule(JavaTimeModule())
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

		return objectMapper
	}

	private fun mapToCookies(cookieMap: Map<String, String>): Array<Cookie> =
		cookieMap.map { (name, value) -> Cookie(name, value) }.toTypedArray()

	protected fun print(
		title: String,
		vararg snippets: Snippet,
	): RestDocumentationResultHandler = document(title, getDocumentRequest(), getDocumentResponse(), *snippets)

	private fun getDocumentRequest(): OperationRequestPreprocessor =
		preprocessRequest(
			prettyPrint(),
			modifyUris().host("dokbaro.com").removePort(),
		)

	private fun getDocumentResponse(): OperationResponsePreprocessor = preprocessResponse(prettyPrint())
}