package kr.kro.dokbaro.server.core.member.domain

data class Emails(
	val mainEmail: EmailAddress,
	val subEmail: Set<EmailAddress> = emptySet(),
)