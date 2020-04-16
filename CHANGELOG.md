# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- STAC spec v0.9.0-RC1 updates

## [0.9.0.0] - 2019-10-30
### Added
- STAC spec v0.8.0 updates
- Upgrade to Spring Boot 2.2.0

## [0.7.0.5] - 2019-06-12
### Added
- Official sort extension support

### Changed

### Removed

## [0.7.0.5] - 2019-06-12
### Added
- Official filter extension support

### Changed
- Fixed paging bug

### Removed

## [0.7.0] - 2019-05-15
### Added
- Added `ids` and `collections` query parameters to search API
- Added link to search endpoint from root catalog
- Added javax.activation dependency to maven docker build plugin

### Changed
- Updated Staccato to version 0.7.0
- Updated all STAC version references to 0.7.0
- Moved `collections` from properties to item root
- Made `query` clauses default to searching properties and not root fieldsExtension
- Refactored `next` parameter to `page`
- Updated docker image to Amazon Corretto 11
- Upgraded Spring Boot from 2.1.3 to 2.1.5
- Upgraded Spring Cloud version from Greenwich.RELEASE to Greenwich.SR1

### Removed

