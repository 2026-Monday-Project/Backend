<div align="center">
  <a href="https://postimg.cc/gXpqFx3m"><img src="https://i.postimg.cc/W3F9Zr3F/aikon.jpg" alt="Monday 대표 이미지"></a>
</div>

---

## 📅 Monday 프로젝트 개요

### 서비스 소개

Monday는 관객이 사연을 쓰고 다른 관객의 사연에 공감하며, 자연스럽게 공연 예매로 이어지는 **관객참여형 양방향 콘텐츠 공간**을 지향합니다.

#### 🎪 먼데이프로젝트 Monday Project
- 웹사이트: [mondayproject.co.kr](http://mondayproject.co.kr/)
- Instagram: [@mondayprojectkr](https://www.instagram.com/mondayprojectkr/)

#### 🐾 매기스가든 Maggie's Garden
- Instagram: [@maggiesgarden_official](https://www.instagram.com/maggiesgarden_official/)

> **개발 기간**: 2026.07.29 ~ 2026.09.16

---

## 👥 백엔드 팀원 소개

<table align="center">
  <thead>
    <tr>
      <th>이수진</th>
      <th>김나은</th>
      <th>유수빈</th>
      <th>배서연</th>
      <th>김규린</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center"><img src="https://avatars.githubusercontent.com/leewatertrue" alt="이수진님 사진" width="150" height="150"></td>
      <td align="center"><img src="https://avatars.githubusercontent.com/naeuun" alt="김나은님 사진" width="150" height="150"></td>
      <td align="center"><img src="https://avatars.githubusercontent.com/b1nnnnid" alt="유수빈님 사진" width="150" height="150"></td>
      <td align="center"><img src="https://avatars.githubusercontent.com/seoyeon615" alt="배서연님 사진" width="150" height="150"></td>
      <td align="center"><img src="https://avatars.githubusercontent.com/Kimgyurin5111" alt="김규린님 사진" width="150" height="150"></td>
    </tr>
    <tr>
      <td align="center"><a href="https://github.com/leewatertrue">@leewatertrue</a></td>
      <td align="center"><a href="https://github.com/naeuun">@naeuun</a></td>
      <td align="center"><a href="https://github.com/b1nnnnid">@b1nnnnid</a></td>
      <td align="center"><a href="https://github.com/seoyeon615">@seoyeon615</a></td>
      <td align="center"><a href="https://github.com/Kimgyurin5111">@Kimgyurin5111</a></td>
    </tr>
  </tbody>
</table>

---

## ⚙️ 기술 스택

<div align="center">
<table width="100%">
<tr>
<th align="center">Backend</th>
<td align="left">
<img src="https://skillicons.dev/icons?i=java,spring,gradle,idea" alt="Java, Spring Boot, Gradle, IntelliJ">
</td>
</tr>
<tr>
<th align="center">Database</th>
<td align="left">
<img src="https://skillicons.dev/icons?i=mysql" alt="MySQL">
</td>
</tr>
<tr>
<th align="center">API Docs</th>
<td align="left">
<img height="40" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/swagger.png" title="Swagger">
</td>
</tr>
<tr>
<th align="center">Collaboration</th>
<td align="left">
<img src="https://skillicons.dev/icons?i=git,github,figma,notion" alt="Git, GitHub, Figma, Notion">
</td>
</tr>
<tr>
<th align="center">CI/CD</th>
<td align="left">추가 예정</td>
</tr>
<tr>
<th align="center">Deployment</th>
<td align="left">추가 예정</td>
</tr>
</table>
</div>

---

## 🧩 서버 아키텍처

추가 예정

---

## 🗂️ ERD

<div align="center">
  <a href="https://postimg.cc/gxNfypBw"><img src="https://i.postimg.cc/YSHMHq6x/image.png" alt="Monday ERD"></a>
</div>

---

## 🌿 브랜치 전략 & 커밋 컨벤션

### 🌱 브랜치 구조

`develop` 없이, **GitHub Flow** 기반의 구조로 운영합니다.

```
main                 배포 가능한 상태만 유지 (직접 push 금지)
 ├─ feat/…           새로운 기능 개발
 ├─ fix/…            개발 중 발견된 버그 수정
 └─ hotfix/…         배포 이후 긴급 수정
```

### 🏷️ 브랜치 네이밍

| Prefix | 용도 | 예시 |
|:---:|---|---|
| `main` | 배포용 브랜치 (직접 건드리지 않음) | `main` |
| `feat/` | 새로운 기능 개발 | `feat/12/nickname-check` |
| `fix/` | 개발 중 버그 수정 | `fix/8/typo-correction` |
| `hotfix/` | 배포 후 긴급 수정 | `hotfix/3/server-down` |

### ✏️ 커밋 컨벤션

`{타입}: {제목}` 형식을 사용합니다.

| 타입 | 의미 |
|---|---|
| `start` | 프로젝트 초기 세팅 |
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `design` | UI/CSS 등 디자인 변경 |
| `refactor` | 코드 리팩토링 |
| `settings` | 설정 파일 변경 |
| `comment` | 주석 추가·변경 |
| `dependency` | 의존성/플러그인 추가 |
| `docs` | 문서 수정 |
| `merge` | 브랜치 병합 |
| `deploy` | 배포 관련 작업 |
| `rename` | 파일·폴더명 이동/수정 |
| `remove` | 파일 삭제 |
| `revert` | 이전 버전으로 롤백 |
| `test` | 테스트 코드 작성 |
