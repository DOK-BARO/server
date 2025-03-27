package kr.kro.dokbaro.server.core.account.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import kr.kro.dokbaro.server.configuration.annotation.RestDocsTest
import kr.kro.dokbaro.server.configuration.docs.FieldType
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsExecutor
import kr.kro.dokbaro.server.core.account.adapter.input.web.dto.ChangePasswordRequest
import kr.kro.dokbaro.server.core.account.adapter.input.web.dto.IssueTemporaryPasswordRequest
import kr.kro.dokbaro.server.core.account.application.port.input.ChangePasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.IssueTemporaryPasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.security.jwt.JwtResponse
import kr.kro.dokbaro.server.security.jwt.JwtTokenGenerator
import kr.kro.dokbaro.server.security.jwt.cookie.JwtHttpCookieInjector
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@RestDocsTest
@WebMvcTest(AccountController::class)
class AccountControllerTest : StringSpec() {
	override fun extensions() = listOf(SpringExtension)

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

	@Autowired
	lateinit var mvc: MockMvc

	init {

		"이메일 회원가입을 수행한다" {
			every { registerEmailAccountUseCase.registerEmailAccount(any()) } returns UUID.randomUUID()
			every { jwtTokenGenerator.generate(any()) } returns JwtResponse("", "")
			every { jwtHttpCookieInjector.inject(any(), any()) } returns Unit

			RestDocsExecutor(mvc, HttpMethod.POST, Path("/accounts/email")) {
				given {
					body {
						RegisterEmailAccountCommand(
							email = "example@example.com",
							nickname = "exampleNickname",
							password = "securePassword123",
							profileImage = "https://example.com/profile.jpg",
						)
					}
				}
				then {
					expect { status().isCreated }
					docsTitle { "account/register-email-account" }
					requestFields {
						"email" type FieldType.STRING means "이메일"
						"nickname" type FieldType.STRING means "닉네임"
						"password" type FieldType.STRING means "비밀번호"
						"profileImage" type FieldType.STRING means "사용자의 프로필 이미지 URL" optional true
					}
				}
			}
		}

		"임시 비밀번호를 새로 발급받는다" {
			every { issueTemporaryPasswordUseCase.issueTemporaryPassword(any()) } returns Unit

			RestDocsExecutor(mvc, HttpMethod.POST, Path("/accounts/email/issue-temporary-password")) {
				given {
					body {
						IssueTemporaryPasswordRequest(
							email = "example@example.com",
						)
					}
				}
				then {
					docsTitle { "account/issue-temporary-password" }
					expect { status().isNoContent }
					requestFields {
						"email" type FieldType.STRING means "이메일"
					}
				}
			}
		}

		"비밀번호를 변경한다" {
			every { changePasswordUseCase.changePassword(any()) } returns Unit

			RestDocsExecutor(mvc, HttpMethod.PUT, Path("/accounts/email/password")) {
				given {
					body {
						ChangePasswordRequest(
							oldPassword = "before",
							newPassword = "after",
						)
					}
				}
				then {
					docsTitle { "account/change-password" }
					expect { status().isNoContent }
					requestFields {
						"oldPassword" type FieldType.STRING means "before password"
						"newPassword" type FieldType.STRING means "new password"
					}
				}
			}
		}
	}
}