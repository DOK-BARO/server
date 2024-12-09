package kr.kro.dokbaro.server.core.studygroup.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupCollectionPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupDetailPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupMemberCollectionPort
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.NotFoundStudyGroupException
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary

class StudyGroupQueryServiceTest :
	StringSpec({
		val readStudyGroupCollectionPort = mockk<ReadStudyGroupCollectionPort>()
		val readStudyGroupMemberCollectionPort = mockk<ReadStudyGroupMemberCollectionPort>()
		val findStudyGroupDetailPort = mockk<ReadStudyGroupDetailPort>()

		val studyGroupQueryService =
			StudyGroupQueryService(
				readStudyGroupCollectionPort,
				readStudyGroupMemberCollectionPort,
				findStudyGroupDetailPort,
			)

		"로그인 사용자가 속한 study group 목록을 탐색한다" {
			every { readStudyGroupCollectionPort.findAllByStudyMemberId(any()) } returns
				listOf(
					StudyGroupSummary(1, "C 스터디", "ccc.png"),
					StudyGroupSummary(2, "JAVA 스터디", "ccc.png"),
					StudyGroupSummary(3, "모각코 합시다", "ccc.png"),
				)

			studyGroupQueryService.findAll(1) shouldNotBe null
		}

		"해당 그룹의 스터디원들은 탐색한다" {
			every { readStudyGroupMemberCollectionPort.findAllStudyGroupMembers(any()) } returns
				listOf(
					StudyGroupMemberResult(1, 1, 1, "n", StudyMemberRole.MEMBER),
					StudyGroupMemberResult(1, 2, 3, "n", StudyMemberRole.MEMBER),
					StudyGroupMemberResult(1, 3, 4, "n", StudyMemberRole.MEMBER),
				)

			studyGroupQueryService.findAllStudyGroupMembers(1) shouldNotBe null
		}

		"스터디 그룹 정보를 조회한다" {
			every { findStudyGroupDetailPort.findStudyGroupDetailBy(any()) } returns
				StudyGroupDetail(
					name = "AI 연구 스터디",
					introduction = "인공지능과 머신러닝에 대해 함께 공부하는 스터디입니다.",
					profileImageUrl = "https://example.com/group-profile.jpg",
					studyMembers =
						listOf(
							StudyGroupDetail.StudyMember(
								id = 1L,
								nickname = "철수",
								profileImageUrl = "https://example.com/member1-profile.jpg",
								role = "LEADER",
							),
							StudyGroupDetail.StudyMember(
								id = 2L,
								nickname = "영희",
								profileImageUrl = "https://example.com/member2-profile.jpg",
								role = "MEMBER",
							),
							StudyGroupDetail.StudyMember(
								id = 3L,
								nickname = "민수",
								profileImageUrl = null,
								role = "MEMBER",
							),
						),
					inviteCode = "ABC123",
					id = 101L,
				)

			studyGroupQueryService.findStudyGroupDetailBy(101) shouldNotBe null
		}

		"스터디 그룹 정보 조회 시 id에 해당하는 스터디 그룹이 없으면 예외를 발생한다" {
			every { findStudyGroupDetailPort.findStudyGroupDetailBy(any()) } returns null

			shouldThrow<NotFoundStudyGroupException> {
				studyGroupQueryService.findStudyGroupDetailBy(2)
			}
		}
	})