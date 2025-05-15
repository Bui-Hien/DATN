import "../src/styles/_app.scss";
import {BrowserRouter} from "react-router-dom";

function App() {
    return (
        <BrowserRouter>
            <MiniDrawer/>
        </BrowserRouter>
    );
}

export default App;
