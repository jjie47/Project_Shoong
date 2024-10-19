const header = document.querySelector('#header');
const logoimg = document.querySelector('#logoimg');

if (window.scrollY > 0) {
    header.classList.add('active');
    logoimg.innerHTML = '<img src="/images/top_logo_b.png" alt="">';
  }
  
document.addEventListener('scroll', () => {
if (window.scrollY > 0) {
    header.classList.add('active');
    logoimg.innerHTML = '<img src="/images/top_logo_b.png" alt="">';
} else {
    header.classList.remove('active');
    logoimg.innerHTML = '<img src="/images/top_logo.png" alt="">';
}
});