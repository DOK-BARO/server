package kr.kro.dokbaro.server.core.studygroup.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.LeaderCannotWithdrawException
import kr.kro.dokbaro.server.core.studygroup.domain.exception.NotExistStudyMemberException

class StudyGroup(
	val id: Long = Constants.UNSAVED_ID,
	var name: String,
	var introduction: String? = null,
	var profileImageUrl: String? = null,
	val studyMembers: MutableSet<StudyMember> = mutableSetOf(),
	val inviteCode: InviteCode,
) {
	companion object {
		fun of(
			name: String,
			introduction: String?,
			profileImageUrl: String?,
			creatorId: Long,
			inviteCode: InviteCode,
		) = StudyGroup(
			name = name,
			introduction = introduction,
			profileImageUrl = profileImageUrl,
			studyMembers =
				mutableSetOf(
					StudyMember(
						creatorId,
						StudyMemberRole.LEADER,
					),
				),
			inviteCode = inviteCode,
		)
	}

	fun join(participantId: Long) {
		studyMembers.add(
			StudyMember(
				memberId = participantId,
				role = StudyMemberRole.MEMBER,
			),
		)
	}

	fun modify(
		name: String,
		introduction: String?,
		profileImageUrl: String?,
	) {
		this.name = name
		this.introduction = introduction
		this.profileImageUrl = profileImageUrl
	}

	fun changeStudyLeader(newLeaderId: Long) {
		val beforeLeader: StudyMember? = studyMembers.find { it.role == StudyMemberRole.LEADER }
		beforeLeader?.role = StudyMemberRole.MEMBER

		val newLeader: StudyMember =
			studyMembers.find { it.memberId == newLeaderId } ?: throw NotExistStudyMemberException()
		newLeader.role = StudyMemberRole.LEADER
	}

	fun withdrawMember(memberId: Long) {
		if (studyMembers.any { it.memberId == memberId && it.role == StudyMemberRole.LEADER }) {
			throw LeaderCannotWithdrawException()
		}

		studyMembers.removeIf { it.memberId == memberId }
	}
}