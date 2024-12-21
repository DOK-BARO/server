package kr.kro.dokbaro.server.core.studygroup.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
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
	})