### 실행 방법


1. OS 환경 별 docker 설치

   https://www.docker.com/get-started/  


2. 로컬 database 설치 및 설정 

    ```bash
    docker run -d --name your-mysql-container -e MYSQL_ROOT_PASSWORD={패스워드 설정} -p 3306:3306 mysql:8
    ```

    ```bash
    $ docker exec -it mysql-container bash
    $ mysql -u root -p
    
    //Enter password
    
    $ CREATE DATABASE notion
    $ USE notion
    ```

3. 소스코드 내려받기

    ```bash
    $ git clone https://github.com/Siren-repo/Notion_Breadcrumbs.git
    $ cd Notion_Breadcrumbs
    ```

4. application.properties 환경변수 설정

    ```bash
    DATASOURCE_USERNAME = root
    DATASOURCE_PASSWORD = {패스워드 설정}
    DATASOURCE_URL = localhost:3306/notion
    ```

5. 빌드 후 실행

    ```bash
    $ gradle clean build
    $ java -jar build/libs/notion-0.0.1-SNAPSHOT.jar
    ```
---

### 테이블 구조

```sql
CREATE TABLE page (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT,
    title VARCHAR(255) NOT NULL,
    contents TEXT NOT NULL,
    INDEX idx_parent_id (parent_id)
);
```

  ✅ page의 id로 조회하여 관련된 부모 페이지들을 연달아 조회해야하기 때문에 자신의 id와 부모 id를 함께 저장하도록 함

  ✅ 조회 조건인 id(Primary Key Index), 조인 조건인 parenet_id에 인덱스를 적용하여 조회 시 성능 확보

---
### 비즈니스 로직


로직에 대한 고민은 [Issue #3](https://github.com/Siren-repo/Notion_Breadcrumbs/issues/3)에서 확인할 수 있습니다.

- PageService

    ```java
    public PageResponse getPage(Long id){
            Optional<PageEntity> pageOptional = pageRepository.findById(id);
            PageEntity page = pageOptional.orElseThrow(() -> new RuntimeException("Page not found"));
            List<PageDto> subPages = pageRepository.findSubPagesById(page.getParentId());
            String breadCrumbs = pageRepository.findBreadCrumbsById(id);
    
            return PageResponse.builder()
                    .pageId(page.getId())
                    .title(page.getTitle())
                    .subpages(subPages)
                    .breadCrumbs(breadCrumbs)
                    .build();
        }
    ```

    - 요청 온 Path의 id로 page를 찾고 해당 id로 BreadCrumbs를 조회하고, page의 parentId로 subPage를 찾아 Response 객체로 만들어서 반환  
  


- PageRepository
    - BreadCrumbs 조회 시 Recursive Query 사용

        ```java
        WITH RECURSIVE breadcrumbs AS (
            SELECT id, parent_id, title, AS breadcrumbs
            FROM page
            WHERE id = :id
        
            UNION ALL
        
            SELECT p.id, p.parent_id, CONCAT(b.breadcrumbs, ' / ', p.title) AS breadcrumbs
            FROM page AS p
            JOIN breadcrumbs AS b ON p.id = b.parent_id
        )
        SELECT breadcrumbs
        FROM breadcrumbs
        ORDER BY id;
        ```

      ✅ 자신의 id와 부모 Page id를 함께 가지고 있는 계층 구조이므로, 재귀쿼리로 해당 parent_id로 부모의 객체까지 한번에 조회  

      ✅ 조회한 Page에서 관련된 부모 Page가 없을때까지 계속해서 찾는 것 보다 처음부터 연관된 Page들을 한번에 가져오는 것이 다건 조회 시 유리하다고 판단

---
### API 명세


- GET /api/pages/{pageId}

  ➡️ Response : 200 OK

    ```json
    {
        "pageId": 11,
        "title": "K",
        "subpages": [
            {
                "id": 10,
                "parentId": 7,
                "title": "J",
                "contents": "This is Sub Page 1-3-2-1"
            },
            {
                "id": 11,
                "parentId": 7,
                "title": "K",
                "contents": "This is Sub Page 1-3-2-1"
            }
        ],
        "breadCrumbs": "A / D / G / K"
    }
    ```
