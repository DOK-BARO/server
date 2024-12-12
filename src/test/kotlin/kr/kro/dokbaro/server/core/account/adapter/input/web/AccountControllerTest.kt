package kr.kro.dokbaro.server.core.account.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.account.adapter.input.web.dto.ChangePasswordRequest
import kr.kro.dokbaro.server.core.account.adapter.input.web.dto.IssueTemporaryPasswordRequest
import kr.kro.dokbaro.server.core.account.application.port.input.ChangePasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.IssueTemporaryPasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.security.jwt.JwtHttpCookieInjector
import kr.kro.dokbaro.server.security.jwt.JwtResponse
import kr.kro.dokbaro.server.security.jwt.JwtTokenGenerator
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(AccountController::class)
class AccountControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var registerEmailAccountUseCase: RegisterEmailAccountUseCase

	@MockkBean
	lateinit var jwtTokenGenerator: JwtTokenGenerator

	@MockkBean
	lateinit var jwtHttpCookieInjector: JwtHttpCookieInjector

	@MockkBean
	lateinit var issueTemporaryPasswordUseCase: IssueTemporaryPasswordUseCase

	@MockkBean
	lateinit var changePasswordUseCase: ChangePasswordUseCase

	init {

		"이메일 회원가입을 수행한다" {
			every { registerEmailAccountUseCase.registerEmailAccount(any()) } returns UUID.randomUUID()
			every { jwtTokenGenerator.generate(any()) } returns JwtResponse("", "")
			every { jwtHttpCookieInjector.inject(any(), any()) } returns Unit
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

		"임시 비밀번호를 새로 발급받는다" {
			every { issueTemporaryPasswordUseCase.issueTemporaryPassword(any()) } returns Unit

			val body =
				IssueTemporaryPasswordRequest(
					email = "example@example.com",
				)

			performPost(Path("/accounts/email/issue-temporary-password"), body)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"account/issue-temporary-password",
						requestFields(
							fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
						),
					),
				)
		}

		"비밀번호를 변경한다" {
			every { changePasswordUseCase.changePassword(any()) } returns Unit

			val body =
				ChangePasswordRequest(
					oldPassword = "before",
					newPassword = "after",
				)

			performPut(Path("/accounts/email/password"), body)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"account/change-password",
						requestFields(
							fieldWithPath("oldPassword").type(JsonFieldType.STRING).description("before password"),
							fieldWithPath("newPassword").type(JsonFieldType.STRING).description("new password"),
						),
					),
				)
		}
	}
}