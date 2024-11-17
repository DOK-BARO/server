package kr.kro.dokbaro.server.core.termsofservice.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindAgreeAllRequiredTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.query.AgreeAllRequired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TermsOfServiceQueryController::class)
class TermsOfServiceQueryControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findAgreeAllRequiredTermsOfServiceUseCase: FindAgreeAllRequiredTermsOfServiceUseCase

	init {
		"login user의 필수 동의 여부를 확인한다" {
			every { findAgreeAllRequiredTermsOfServiceUseCase.findBy(any()) } returns
				AgreeAllRequired(true)

			performGet(Path("/terms-of-services/member-agree/required"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"terms-of-service/get-member-agree-required",
						responseFields(
							fieldWithPath("agreeAll")
								.type(JsonFieldType.BOOLEAN)
								.description("전체 동의 여부"),
						),
					),
				)
		}
	}
}