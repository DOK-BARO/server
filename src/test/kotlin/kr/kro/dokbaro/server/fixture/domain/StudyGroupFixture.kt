package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.studygroup.domain.InviteCode
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole

fun studyGroupFixture(
	name: String = "Name",
	introduction: String? = "Introduction",
	profileImageUrl: String? = null,
	studyMembers: MutableSet<StudyMember> = mutableSetOf(StudyMember(1, StudyMemberRole.LEADER)),
	inviteCode: InviteCode = InviteCode("ABC123"),
	id: Long = Constants.UNSAVED_ID,
) = StudyGroup(
	name,
	introduction,
	profileImageUrl,
	studyMembers,
	inviteCode,
	id,
)