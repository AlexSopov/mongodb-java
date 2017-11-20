# Mongodb java
In this repository presents unsing of Java integration to Mongodb. Realised different queries, including map reduce paradigm.

[![Build Status](https://travis-ci.org/AlexSopov/mongodb-java.svg?branch=master)](https://travis-ci.org/AlexSopov/mongodb-java)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6aaf74fae035409fae95220fb7506ec9)](https://www.codacy.com/app/AlexSopov/mongodb-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=AlexSopov/mongodb-java&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/6aaf74fae035409fae95220fb7506ec9)](https://www.codacy.com/app/AlexSopov/mongodb-java?utm_source=github.com&utm_medium=referral&utm_content=AlexSopov/mongodb-java&utm_campaign=Badge_Coverage)
[![codecov](https://codecov.io/gh/AlexSopov/mongodb-java/branch/master/graph/badge.svg)](https://codecov.io/gh/AlexSopov/mongodb-java)

### Processed data
1. User ip: 109.108.22.11

| Visited URL     | Visited time* | Time spent** |
|:----------------|:-------------:|:------------:|
| google.com      | 1511036876282 | 1000000      |
| github.com      | 1510950476311 | 5000000      |
| aliexpress.com  | 1510864076311 | 3000000      |
| google.com      | 1510777676311 | 2000000      |
| google.com      | 1510691276311 | 1000000      |

2. User ip: 105.132.12.15

| Visited URL     | Visited time  | Time spent   |
|:----------------|:-------------:|:------------:|
| github.com      | 1511036876282 | 8000000      |
| rozetka.ua      | 1510950476311 | 1000000      |
| google.com      | 1510777676311 | 3000000      |
| aliexpress.com  | 1510691276311 | 9000000      |

3. User ip: 219.108.32.12

| Visited URL     | Visited time  | Time spent   |
|:----------------|:-------------:|:------------:|
| facebook.com    | 1511036876282 | 5000000      |
| github.com      | 1510950476311 | 2000000      |
| aliexpress.com  | 1510864076311 | 1000000      |
| rozetka.ua      | 1510691276311 | 7000000      |

4. User ip: 109.101.72.13

| Visited URL     | Visited time  | Time spent   |
|:----------------|:-------------:|:------------:|
| facebook.com    | 1511036876282 | 4000000      |
| github.com      | 1510777676311 | 1000000      |


5. User ip: 179.180.22.17

| Visited URL     | Visited time  | Time spent   |
|:----------------|:-------------:|:------------:|
| google.com      | 1510950476311 | 6000000      |
| github.com      | 1510777676311 | 7000000      |
| aliexpress.com  | 1510691276311 | 4000000      |

\* Unix timee
\** In milliseconds

### Results of executing
* Выдать упорядоченный список IP-адресов пользователей,посетивших ресурс с заданным URL. *URL: google.com*

| Ip                 |
|:-------------------|
| 105.132.12.15      |
| 109.108.22.11      |
| 179.180.22.17      |

* Выдать упорядоченный список URL ресурсов, посещенных в заданный временной период. *Period: [1511036876282, 1510950476311]*

| URL                |
|:-------------------|
| facebook.com       |
| github.com         |
| google.com         |
| rozetka.ua         |

* Выдать упорядоченный список URL ресурсов, посещенных пользователем сзаданным IP-адресом. *IP: 109.101.72.13*

| URL                |
|:-------------------|
| facebook.com       |
| github.com         |


* Выдать список URL ресурсов с указанием суммарной длительности посещения каждого ресурса, упорядоченный по убыванию.

| Visited URL     | Total time(s) |
|:----------------|:--------------|
| github.com      | 23000 |
| aliexpress.com  | 17000 |
| facebook.com    | 11000 |
| google.com      | 10000 |
| rozetka.ua      | 9000 |

* Выдать список URL ресурсов с указанием суммарного количества посещений каждого ресурса, упорядоченный по убыванию.

| Visited URL     | Total count |
|:----------------|:--------------|
| github.com      | 5 |
| aliexpress.com  | 4 |
| facebook.com    | 3 |
| google.com      | 3 |
| rozetka.ua      | 3 |

* Выдать список URL ресурсов с казанием количества посещений каждого ресурса в день за заданный период, упорядоченный URL ресурса и
убыванию количества посещений. *Period: [1511036876282, 1510950476311]*

| Visited URL     | Total count |
|:----------------|:--------------|
| github.com      | 3 |
| facebook.com    | 2 |
| google.com      | 2 |
| rozetka.ua      | 1 |

* Выдать список IP-адресов c указанием суммарного количества и суммарной длительности посещений ресурсов, упорядоченный по адресу, убыванию количества и убыванию длительности.


| Ip              | Url           |Visit count| Duration|
|:----------------|:--------------|:----------|:-----------|
|                 | aliexpress.com |1|9000000
|105.132.12.15    | github.com |1|8000000
|                 | google.com |1|3000000
|                 | rozetka.ua |1|1000000
|...              | ... |...|...
