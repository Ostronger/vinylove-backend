import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";



export default function LoginPage() {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const API_URL = import.meta.env.VITE_API_URL;
    const [error, setError] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        const role = localStorage.getItem("role");

        if (token && role === "ADMIN") {
            navigate("/admin");
        }

        if (token && role === "STAFF") {
            navigate("/staff");
        }
    }, [navigate]);


    const handleSubmit = async (e: React.SyntheticEvent) => {
        e.preventDefault();
        setError("");
        // Add login logic here
        const response = await fetch(`${API_URL}/api/users/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();
            
            localStorage.setItem("accessToken", data.accessToken);
            localStorage.setItem("refreshToken", data.refreshToken);
            localStorage.setItem("role", data.role);
            localStorage.setItem("user", JSON.stringify(data));

            if (data.role === "ADMIN") {
                navigate("/admin");
            } else if (data.role === "STAFF") {
                navigate("/staff");
            }
            // Redirect to dashboard or store token as needed
        } else {
            setError("Email ou mot de passe incorrect.");
        }
    };

    return (
        <div>
            <h1>Login Page</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                {error && <p style={{ color: "red" }}>{error}</p>}
                <button type="submit">Login</button>
            </form>
        </div>
    );
}