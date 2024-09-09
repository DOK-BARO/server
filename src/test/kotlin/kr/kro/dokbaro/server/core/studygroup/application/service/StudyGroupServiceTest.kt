package kr.kro.dokbaro.server.core.studygroup.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.dto.MemberResponse
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.out.SaveStudyGroupPort
import java.util.UUID

class StudyGroupServiceTest :
	StringSpec({
		val saveStudyGroupPort = mockk<SaveStudyGroupPort>()
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()

		val studyGroupService = StudyGroupService(saveStudyGroupPort, findCertificatedMemberUseCase)
		"study group을 생성한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns
				MemberResponse(
					nickName = "test",
					email = "test@test.com",
					profileImage = "test.png",
					certificationId = UUID.randomUUID(),
					roles = setOf(Role.GUEST),
					id = 1,
				)

			every { saveStudyGroupPort.save(any()) } returns 1

			val command =
				CreateStudyGroupCommand(
					name = "test",
					introduction = "test",
					profileImageUrl = "profile.png",
					creatorAuthId = UUID.randomUUID(),
				)

			studyGroupService.create(command) shouldNotBe null
		}
	})