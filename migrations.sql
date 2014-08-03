
CREATE TABLE `builds` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `buildKey` varchar(150) NOT NULL DEFAULT '',
  `status` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `startTime` bigint(11) NOT NULL,
  `duration` int(11) NOT NULL,
  `gitUser` varchar(150) NOT NULL DEFAULT '',
  `gitRepo` varchar(150) NOT NULL DEFAULT '',
  `gitBranch` varchar(150) NOT NULL DEFAULT '',
  `lastGitCommitSha` char(40) NOT NULL DEFAULT '',
  `artifactLocation` varchar(255) NOT NULL DEFAULT '',
  `bad` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `buildKey_2` (`buildKey`,`number`),
  KEY `startTime` (`startTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `deploys` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `action` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `deployKey` varchar(150) NOT NULL DEFAULT '',
  `buildKey` varchar(150) NOT NULL DEFAULT '',
  `buildNumber` int(11) NOT NULL,
  `environment` varchar(150) NOT NULL DEFAULT '',
  `startTime` bigint(20) NOT NULL,
  `duration` int(11) NOT NULL,
  `rollbackDeployId` int(11) DEFAULT NULL,
  `deployConfigSnapshot` text NOT NULL,
  `user` varchar(150) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `deployKey` (`deployKey`,`buildKey`,`buildNumber`),
  KEY `environment` (`environment`),
  KEY `startTime` (`startTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `deploy_registry` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `key` varchar(150) NOT NULL DEFAULT '',
  `gitUser` varchar(150) NOT NULL DEFAULT '',
  `gitRepo` varchar(150) NOT NULL DEFAULT '',
  `gitBranch` varchar(150) NOT NULL DEFAULT '',
  `path` varchar(150) NOT NULL DEFAULT '',
  `lastGitCommitSha` char(40) NOT NULL DEFAULT '',
  `lastUpdated` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `build_registry` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `key` varchar(150) NOT NULL DEFAULT '',
  `gitUser` varchar(150) NOT NULL DEFAULT '',
  `gitRepo` varchar(150) NOT NULL DEFAULT '',
  `gitBranch` varchar(150) NOT NULL DEFAULT '',
  `path` varchar(150) NOT NULL DEFAULT '',
  `lastGitCommitSha` char(40) NOT NULL DEFAULT '',
  `lastUpdated` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;