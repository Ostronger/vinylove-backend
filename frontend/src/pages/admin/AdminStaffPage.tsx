import { useEffect, useState } from "react";

const API_URL = import.meta.env.VITE_API_URL;

interface User {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    role: "ADMIN" | "STAFF";
    createdAt?: string;
}

export default function AdminStaffPage() {
    const [staffUsers, setStaffUsers] = useState<User[]>([]);

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    useEffect(() => {
    const fetchStaffUsers = async () => {
        const token = localStorage.getItem("accessToken");

        const response = await fetch(`${API_URL}/api/users`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        const data = await response.json();

        if (Array.isArray(data)) {
            setStaffUsers(data.filter((user) => user.role === "STAFF"));
        }
    };

    
        fetchStaffUsers();
    }, []);

    const handleCreateStaff = async (e: React.FormEvent) => {
        e.preventDefault();

        const token = localStorage.getItem("accessToken");

        const response = await fetch(`${API_URL}/api/users`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
                firstName,
                lastName,
                email,
                password,
                role: "STAFF",
            }),
        });

        if (response.ok) {
            
            const createdUser = await response.json();
            if (createdUser.role === "STAFF") {
                setStaffUsers((previousStaff) => [...previousStaff, createdUser]);
            }
            setFirstName("");
            setLastName("");
            setEmail("");
            setPassword("");
        }
    };

    const handleDeleteStaff = async (userId: number) => {
        const confirmDelete = window.confirm("Êtes-vous sûr de vouloir supprimer ce staff ?");
        if (!confirmDelete) return;

        const token = localStorage.getItem("accessToken");

        const response = await fetch(`${API_URL}/api/users/${userId}`, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (response.ok) {
            setStaffUsers((previousStaff) =>
                previousStaff.filter((user) => user.id !== userId)
            );
        }
    };

    return (
        <div>
            <h1>Gestion du staff</h1>

            <form onSubmit={handleCreateStaff}>
                <input
                    type="text"
                    placeholder="Prénom"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                />

                <input
                    type="text"
                    placeholder="Nom"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                />

                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Mot de passe"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button type="submit">Créer un staff</button>
            </form>

            <h2>Liste des staffs</h2>

            {staffUsers.map((user) => (
                <div key={user.id}>
                    <p>
                        {user.firstName} {user.lastName}
                    </p>
                    <p>{user.email}</p>
                    <button onClick={() => handleDeleteStaff(user.id)}>Supprimer</button>
                </div>
            ))}
        </div>
    );
}