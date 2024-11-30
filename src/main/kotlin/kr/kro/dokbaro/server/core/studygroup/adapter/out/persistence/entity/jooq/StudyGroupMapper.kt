package kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.core.studygroup.domain.InviteCode
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JStudyGroupMember
import org.jooq.generated.tables.records.StudyGroupRecord

@Mapper
class StudyGroupMapper {
	companion object {
		private val STUDY_GROUP_MEMBER = JStudyGroupMember.STUDY_GROUP_MEMBER
		private val MEMBER = JMember.MEMBER
	}

	fun recordToStudyGroup(record: Map<StudyGroupRecord, Result<Record>>): StudyGroup? =
		record
			.map { (group, members) ->
				StudyGroup(
					name = group.name,
					introduction = group.introduction,
					profileImageUrl = group.profileImageUrl,
					studyMembers =
						members
							.map {
								StudyMember(
									memberId = it.getValue(STUDY_GROUP_MEMBER.MEMBER_ID),
									role = StudyMemberRole.valueOf(it.getValue(STUDY_GROUP_MEMBER.MEMBER_ROLE)),
								)
							}.toMutableSet(),
					inviteCode = InviteCode(group.inviteCode),
					id = group.id,
				)
			}.firstOrNull()

	fun toStudyGroupSummary(record: Result<StudyGroupRecord>): Collection<StudyGroupSummary> =
		record.map {
			StudyGroupSummary(
				id = it.id,
				name = it.name,
				profileImageUrl = it.profileImageUrl,
			)
		}

	fun toStudyGroupMemberResult(record: Result<out Record>): Collection<StudyGroupMemberResult> =
		record.map {
			StudyGroupMemberResult(
				it[STUDY_GROUP_MEMBER.STUDY_GROUP_ID],
				it[STUDY_GROUP_MEMBER.ID],
				it[STUDY_GROUP_MEMBER.MEMBER_ID],
				it[MEMBER.NICKNAME],
				StudyMemberRole.valueOf(it[STUDY_GROUP_MEMBER.MEMBER_ROLE]),
			)
		}

	fun toStudyGroupDetail(record: Map<StudyGroupRecord, Result<out Record>>): StudyGroupDetail? =
		record
			.map { (studyGroup, members) ->
				StudyGroupDetail(
					id = studyGroup.id,
					name = studyGroup.name,
					introduction = studyGroup.introduction,
					profileImageUrl = studyGroup.profileImageUrl,
					studyMembers =
						members.map {
							StudyGroupDetail.StudyMember(
								id = it[STUDY_GROUP_MEMBER.MEMBER_ID],
								nickname = it[MEMBER.NICKNAME],
								profileImageUrl = it[MEMBER.PROFILE_IMAGE_URL],
								role = it[STUDY_GROUP_MEMBER.MEMBER_ROLE],
							)
						},
					inviteCode = studyGroup.inviteCode,
				)
			}.firstOrNull()
}