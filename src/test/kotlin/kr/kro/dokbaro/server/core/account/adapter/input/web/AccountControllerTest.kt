package kr.kro.dokbaro.server.core.account.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AccountController::class)
class AccountControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var registerEmailAccountUseCase: RegisterEmailAccountUseCase

	init {

		"이메일 회원가입을 수행한다" {
			every { registerEmailAccountUseCase.registerEmailAccount(any()) } returns Unit

			val command =
				RegisterEmailAccountCommand(
					email = "example@example.com",
					nickname = "exampleNickname",
					password = "securePassword123",
					profileImage = "https://example.com/profile.jpg",
				)

			performPost(Path("/accounts/email"), command)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"account/register-email-account",
						requestFields(
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("이메일"),
							fieldWithPath("nickname")
								.type(JsonFieldType.STRING)
								.description("닉네임"),
							fieldWithPath("password")
								.type(JsonFieldType.STRING)
								.description("비밀번호"),
							fieldWithPath("profileImage")
								.type(JsonFieldType.STRING)
								.optional()
								.description("사용자의 프로필 이미지 URL. (optional)"),
						),
					),
				)
		}
	}
}