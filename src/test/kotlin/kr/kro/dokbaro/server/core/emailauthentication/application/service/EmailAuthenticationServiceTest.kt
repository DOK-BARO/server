package kr.kro.dokbaro.server.core.emailauthentication.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.dto.MatchResponse
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.ExistEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.InsertEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.LoadEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.SendEmailAuthenticationCodePort
import kr.kro.dokbaro.server.fixture.domain.emailAuthenticationFixture

class EmailAuthenticationServiceTest :
	StringSpec({
		val existEmailAuthenticationPort = mockk<ExistEmailAuthenticationPort>()
		val insertEmailAuthenticationPort = mockk<InsertEmailAuthenticationPort>()
		val loadEmailAuthenticationPort = mockk<LoadEmailAuthenticationPort>()
		val updateEmailAuthenticationPort = UpdateEmailAuthenticationPortMock()
		val emailCodeGenerator = CodeGeneratorStub()
		val sendEmailAuthenticationCodePort = mockk<SendEmailAuthenticationCodePort>()

		val emailAuthenticationService =
			EmailAuthenticationService(
				existEmailAuthenticationPort,
				insertEmailAuthenticationPort,
				loadEmailAuthenticationPort,
				updateEmailAuthenticationPort,
				emailCodeGenerator,
				sendEmailAuthenticationCodePort,
			)

		afterEach {
			clearAllMocks()

			updateEmailAuthenticationPort.clear()
		}

		"생성을 수행한다" {
			every { existEmailAuthenticationPort.existBy(any()) } returns false
			every { insertEmailAuthenticationPort.insert(any()) } returns 1
			every { sendEmailAuthenticationCodePort.sendEmail(any(), any()) } returns Unit

			val email = "www@example.org"
			emailAuthenticationService.create(email)

			verify { sendEmailAuthenticationCodePort.sendEmail(email, any()) }
		}

		"생성 시 이미 미인증된 이메일로 등록되어있으면 인증코드 업데이트만 진행한 후 메일을 전송한다'" {
			val email = "www@example.org"
			val code = "ABC123"
			every { existEmailAuthenticationPort.existBy(any()) } returns true
			every { sendEmailAuthenticationCodePort.sendEmail(any(), any()) } returns Unit
			every { loadEmailAuthenticationPort.findBy(any()) } returns
				emailAuthenticationFixture(
					address = email,
					code = code,
				)

			emailAuthenticationService.create(email)

			updateEmailAuthenticationPort.storage?.code shouldNotBe code

			verify(exactly = 0) { insertEmailAuthenticationPort.insert(any()) }
			verify { sendEmailAuthenticationCodePort.sendEmail(email, any()) }
		}

		"인증 코드를 검증 시 일치하면 인증 여부 상태를 변경하고 true를 반환한다" {
			val email = "www@example.org"
			val code = "ABCDEF"
			every { loadEmailAuthenticationPort.findBy(any()) } returns
				emailAuthenticationFixture(
					address = email,
					code = code,
				)

			val response: MatchResponse = emailAuthenticationService.match(email, code)

			response.result shouldBe true
			updateEmailAuthenticationPort.storage?.authenticated shouldBe true
		}

		"인증 코드를 검증 시 일치하지 않으면 상태를 변경하지 않고 false를 반환한다" {
			val email = "www@example.org"
			val code = "ABCDEF"
			every { loadEmailAuthenticationPort.findBy(any()) } returns
				emailAuthenticationFixture(
					address = email,
					code = code,
				)

			val response: MatchResponse = emailAuthenticationService.match(email, "OTHER3")

			response.result shouldBe false
			updateEmailAuthenticationPort.storage shouldBe null
		}

		"인증코드 검증/재생성/사용 시 이메일 인증 데이터가 없다면 예외를 반환한다" {
			every { loadEmailAuthenticationPort.findBy(any()) } returns null

			val email = "www@example.com"

			shouldThrow<NotFoundEmailAuthenticationException> {
				emailAuthenticationService.match(email, "ASBSDF")
			}

			shouldThrow<NotFoundEmailAuthenticationException> {
				emailAuthenticationService.recreate(email)
			}

			shouldThrow<NotFoundEmailAuthenticationException> {
				emailAuthenticationService.useEmail(email)
			}
		}

		"인증 코드 재생성을 진행한다" {
			val email = "www@example.org"
			val beforeCode = "BEFORE"
			every { loadEmailAuthenticationPort.findBy(any()) } returns
				emailAuthenticationFixture(
					address = email,
					code = beforeCode,
				)
			every { sendEmailAuthenticationCodePort.sendEmail(any(), any()) } returns Unit

			emailAuthenticationService.recreate(email)

			updateEmailAuthenticationPort.storage?.code shouldNotBe beforeCode
			verify { sendEmailAuthenticationCodePort.sendEmail(email, any()) }
		}

		"이메일 사용을 수행한다" {
			val email = "www@example.org"
			every { loadEmailAuthenticationPort.findBy(any()) } returns
				emailAuthenticationFixture(
					address = email,
					authenticated = true,
				)

			emailAuthenticationService.useEmail(email)

			updateEmailAuthenticationPort.storage?.used shouldBe true
		}
	})