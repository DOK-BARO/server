package kr.kro.dokbaro.server.core.member.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class MemberQueryServiceTest :
	StringSpec({

		val loadMemberByCertificationIdPort = mockk<LoadMemberByCertificationIdPort>()

		val memberQueryService = MemberQueryService(loadMemberByCertificationIdPort)

		"certificationId를 통한 조회를 수행한다" {
			every { loadMemberByCertificationIdPort.findByCertificationId(any()) } returns memberFixture()

			memberQueryService.getByCertificationId(UUID.randomUUID()) shouldNotBe null
		}

		"certificationId 에 해당하는 member가 없으면 예외를 반환한다" {
			every { loadMemberByCertificationIdPort.findByCertificationId(any()) } returns null

			assertThrows<NotFoundMemberException> {
				memberQueryService.getByCertificationId(UUID.randomUUID())
			}
		}
	})