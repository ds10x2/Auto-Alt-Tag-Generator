// 모든 img 태그 선택
const imgTags = document.querySelectorAll("img");
console.log(imgTags);
// img 태그를 반복하면서 alt 속성이 없는 경우 수정
imgTags.forEach((img) => {
  const altText = img.getAttribute("alt");
  if (!altText || altText.trim() === "") {
    // alt 속성이 비어있거나 공백 문자열인 경우
    img.setAttribute("alt", "대체 텍스트 테스트");
    console.log("alt 속성이 비어있습니다.");
  }
});
