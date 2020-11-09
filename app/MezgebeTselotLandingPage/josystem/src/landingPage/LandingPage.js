import "./style.css";
import logo from "../logo.svg";
function landingPage() {
  return (
    <div className='container'>
      <header>
        <div className='logo-container'>
          <img className='logo' src={logo} />
          <h2 className='title'>Josystems</h2>
        </div>
        <nav>
          <ul className='menus'>
            <li className='active'>Home</li>
            <li>Service</li>
            <li>Contact</li>
            <li>About</li>
          </ul>
        </nav>
        <input type='checkbox' id='check' />
        <label id='checkbtn' htmlFor='check'>
          &#9776;
        </label>
      </header>
      <div className='app-screen-container'>
        <img className='app-screen' />
      </div>
    </div>
  );
}
export default landingPage;
