package kr.kro.dokbaro.server.global.template.port

interface LoadPort<K, V> {
	fun findBy(k: K): V?
}