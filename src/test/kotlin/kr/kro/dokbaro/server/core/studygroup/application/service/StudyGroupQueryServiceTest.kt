package kr.kro.dokbaro.server.core.studygroup.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupCollectionPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupMemberCollectionPort
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import kr.kro.dokbaro.server.fixture.domain.certificatedMemberFixture
import java.util.UUID

class StudyGroupQueryServiceTest :
	StringSpec({
		val findAllMyStudyGroupUseCase = mockk<FindCertificatedMemberUseCase>()
		val readStudyGroupCollectionPort = mockk<ReadStudyGroupCollectionPort>()
		val readStudyGroupMemberCollectionPort = mockk<ReadStudyGroupMemberCollectionPort>()

		val studyGroupQueryService =
			StudyGroupQueryService(findAllMyStudyGroupUseCase, readStudyGroupCollectionPort, readStudyGroupMemberCollectionPort)

		"로그인 사용자가 속한 study group 목록을 탐색한다" {
			every { findAllMyStudyGroupUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()
			every { readStudyGroupCollectionPort.findAllByStudyMemberId(any()) } returns
				listOf(
					StudyGroupSummary("C 스터디", "ccc.png", 1),
					StudyGroupSummary("JAVA 스터디", "ccc.png", 1),
					StudyGroupSummary("모각코 합시다", "ccc.png", 1),
				)

			studyGroupQueryService.findAll(UUID.randomUUID()) shouldNotBe null
		}

		"해당 그룹의 스터디원들은 탐색한다" {
			every { readStudyGroupMemberCollectionPort.findAllStudyGroupMembers(any()) } returns
				listOf(
					StudyGroupMemberResult(1, 1, 1, "n", StudyMemberRole.MEMBER),
					StudyGroupMemberResult(1, 2, 3, "n", StudyMemberRole.MEMBER),
					StudyGroupMemberResult(1, 3, 4, "n", StudyMemberRole.MEMBER),
				)
		}

		studyGroupQueryService.findAllStudyGroupMembers(1) shouldNotBe null
	})