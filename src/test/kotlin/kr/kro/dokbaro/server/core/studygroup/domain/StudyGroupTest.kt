package kr.kro.dokbaro.server.core.studygroup.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.LeaderCannotWithdrawException
import kr.kro.dokbaro.server.core.studygroup.domain.exception.NotExistStudyMemberException
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture

class StudyGroupTest :
	StringSpec({

		"study group을 생성한 사람은 leader로 초기설정된다" {
			val studyGroup =
				StudyGroup.of(
					name = "name",
					introduction = "introduction",
					profileImageUrl = "profileImage.png",
					creatorId = 4,
					inviteCode = InviteCode(""),
				)

			studyGroup.studyMembers.count() shouldBe 1
			studyGroup.studyMembers.first().role shouldBe StudyMemberRole.LEADER
		}

		"스터디 그룹에 참여한다" {
			val studyGroup =
				StudyGroup.of(
					name = "name",
					introduction = "introduction",
					profileImageUrl = "profileImage.png",
					creatorId = 4,
					inviteCode = InviteCode(""),
				)

			val newMemberId = 5L

			studyGroup.join(newMemberId)

			studyGroup.studyMembers.contains(StudyMember(newMemberId, StudyMemberRole.MEMBER))
		}

		"스터디 정보를 수정한다" {
			val studyGroup = studyGroupFixture()
			val newName = "newName"
			val newIntroduction = "newIntroduction"
			val newProfileImageUrl = "newProfileImageUrl.png"
			studyGroup.modify(
				name = newName,
				introduction = newIntroduction,
				profileImageUrl = newProfileImageUrl,
			)

			studyGroup.name shouldBe newName
			studyGroup.introduction shouldBe newIntroduction
			studyGroup.profileImageUrl shouldBe newProfileImageUrl
		}

		"리더를 변경한다" {
			val studyGroup =
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(
								memberId = 1,
								role = StudyMemberRole.LEADER,
							),
							StudyMember(
								memberId = 2,
								role = StudyMemberRole.MEMBER,
							),
						),
				)

			studyGroup.changeStudyLeader(2)

			studyGroup.studyMembers.first { it.role == StudyMemberRole.LEADER }.memberId shouldBe 2
			studyGroup.studyMembers.first { it.role == StudyMemberRole.MEMBER }.memberId shouldBe 1
		}

		"팀원에 포함되지 않는 사람을 리더로 변경하려 하면 예외를 반환한다" {
			val studyGroup =
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(
								memberId = 1,
								role = StudyMemberRole.LEADER,
							),
							StudyMember(
								memberId = 2,
								role = StudyMemberRole.MEMBER,
							),
						),
				)

			shouldThrow<NotExistStudyMemberException> {
				studyGroup.changeStudyLeader(3)
			}
		}

		"기존에 리더가 없었으면, 새로 지정한다." {
			val studyGroup =
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(
								memberId = 1,
								role = StudyMemberRole.MEMBER,
							),
							StudyMember(
								memberId = 2,
								role = StudyMemberRole.MEMBER,
							),
						),
				)
			studyGroup.changeStudyLeader(2)
			studyGroup.studyMembers.first { it.role == StudyMemberRole.LEADER }.memberId shouldBe 2
			studyGroup.studyMembers.first { it.role == StudyMemberRole.MEMBER }.memberId shouldBe 1
		}

		"스터디 그룹 탈퇴를 수행한다" {
			val studyGroup =
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(
								memberId = 1,
								role = StudyMemberRole.LEADER,
							),
							StudyMember(
								memberId = 2,
								role = StudyMemberRole.MEMBER,
							),
						),
				)

			studyGroup.withdrawMember(memberId = 2)
			studyGroup.studyMembers.count() shouldBe 1
		}

		"리더는 그룹 탈퇴가 불가능하다." {
			val studyGroup =
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(
								memberId = 1,
								role = StudyMemberRole.LEADER,
							),
							StudyMember(
								memberId = 2,
								role = StudyMemberRole.MEMBER,
							),
						),
				)

			shouldThrow<LeaderCannotWithdrawException> {
				studyGroup.withdrawMember(memberId = 1)
			}
		}
	})