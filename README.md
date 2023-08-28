# spring_week1_gallery
html이 없어서 PostMan으로 실행해야 합니다.

저장과 출력에는 큰 문제가 없으나, 
updateAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
를 사용하다보니 Post 요청시 시간값이 null로 반환됩니다.
Get요청에서는 잘 동작합니다.
