package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github.external.resource

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GithubAccount(
	val login: String?,
	val id: Long,
	val nodeId: String?,
	val avatarUrl: String?,
	val gravatarId: String?,
	val url: String?,
	val htmlUrl: String?,
	val followersUrl: String?,
	val followingUrl: String?,
	val gistsUrl: String?,
	val starredUrl: String?,
	val subscriptionsUrl: String?,
	val organizationsUrl: String?,
	val reposUrl: String?,
	val eventsUrl: String?,
	val receivedEventsUrl: String?,
	val type: String?,
	val siteAdmin: Boolean?,
	val name: String?,
	val company: String?,
	val blog: String?,
	val location: String?,
	val email: String?,
	val hireable: String?,
	val twitterUsername: String?,
	val publicRepos: Int?,
	val publicGists: Int?,
	val followers: Int?,
	val following: Int?,
	val createdAt: String?,
	val updatedAt: String?,
	val privateGists: Int?,
	val totalPrivateRepos: Int?,
	val ownedPrivateRepos: Int?,
	val diskUsage: Int?,
	val collaborators: Int?,
	val twoFactorAuthentication: Boolean?,
	val plan: Plan?,
	val bio: String?,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Plan(
	val name: String?,
	val space: Long?,
	val collaborators: Int?,
	val privateRepos: Int?,
)