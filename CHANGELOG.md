# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
## [0.1.0] - 2019-05-15
### Added
- Added `ids` and `collections` query parameters to search API
- Added link to search endpoint from root catalog

### Changed
- Moved `collections` from properties to item root
- Made `query` clauses default to searching properties and not root fields
- Updated all version references to 0.7.0
- Refactored `next` parameter to `page`
- Updated docker image to Amazon Corretto 11

### Removed

