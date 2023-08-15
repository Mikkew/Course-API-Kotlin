package com.mms.kotlin.spring.app.mapper

abstract class AbstractConverter<Entity, Dto> {
    public abstract fun toDto(entity: Entity): Dto;
    public abstract fun toEntity(dto: Dto): Entity;

    public fun toDto(entities: List<Entity>): List<Dto> = entities.map { this.toDto(it) }.toList();
    public fun toEntity(dtos: List<Dto>): List<Entity> = dtos.map { this.toEntity(it) }.toList();
}