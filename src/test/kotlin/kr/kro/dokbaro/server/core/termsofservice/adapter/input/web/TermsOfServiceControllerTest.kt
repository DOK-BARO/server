package kr.kro.dokbaro.server.core.termsofservice.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.AgreeTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindAllTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindTermsOfServiceDetailUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.dto.AgreeTermsOfServiceRequest
import kr.kro.dokbaro.server.core.termsofservice.domain.TermsOfService
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceDetail
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceSummary
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TermsOfServiceController::class)
class TermsOfServiceControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findAllTermsOfServiceUseCase: FindAllTermsOfServiceUseCase

	@MockkBean
	lateinit var findTermsOfServiceDetailUseCase: FindTermsOfServiceDetailUseCase

	@MockkBean
	lateinit var agreeTermsOfServiceUseCase: AgreeTermsOfServiceUseCase

	init {
		"이용약관 목록을 조회한다" {
			every { findAllTermsOfServiceUseCase.findAll() } returns
				TermsOfService.entries.map {
					TermsOfServiceSummary(
						id = it.id,
						title = it.title,
						subTitle = it.subTitle,
						hasDetail = it.hasDetail,
						primary = it.primary,
					)
				}

			performGet(Path("/terms-of-services"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"terms-of-service/find-all",
						responseFields(
							fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("이용 약관 ID"),
							fieldWithPath("[].title").type(JsonFieldType.STRING).description("제목"),
							fieldWithPath("[].subTitle")
								.type(JsonFieldType.STRING)
								.description("부제목 (optional)")
								.optional(),
							fieldWithPath("[].hasDetail").type(JsonFieldType.BOOLEAN).description("상세 내용 존재 여부"),
							fieldWithPath("[].primary").type(JsonFieldType.BOOLEAN).description("필수 여부"),
						),
					),
				)
		}

		"이용약관 상세 내용을 조회한다" {
			every { findTermsOfServiceDetailUseCase.findDetail(any()) } returns
				TermsOfServiceDetail("이용 약관 동의 상세 내용")

			performGet(Path("/terms-of-services/{id}/detail", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"terms-of-service/find-detail",
						pathParameters(parameterWithName("id").description("이용약관 ID")),
						responseFields(
							fieldWithPath("value").type(JsonFieldType.STRING).description("이용 약관 동의 상세 내용"),
						),
					),
				)
		}

		"이용약관에 동의한다" {
			every { agreeTermsOfServiceUseCase.agree(any(), any()) } returns Unit

			val body = AgreeTermsOfServiceRequest(listOf(1, 2, 3))

			performPost(Path("/terms-of-services/agree"), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"terms-of-service/agree",
						requestFields(
							fieldWithPath("items").type(JsonFieldType.ARRAY).description("동의하는 약관의 ID 목록"),
						),
					),
				)
		}
	}
}