import React, { useState } from 'react';

function UserList() {
    // 创建用户状态
    const [users, setUsers] = useState([
        {id: 1, username: '张三', email: 'zhangsan@example.com', createdAt: '2021-01-01'},
        {id: 2, username: '李四', email: 'lisi@example.com', createdAt: '2021-01-02'}
    ]);

    // 表单状态
    const [newUsername, setNewUsername] = useState('');
    const [newEmail, setNewEmail] = useState('');

    // 添加用户函数
    const handleAddUser = (e) => {
        e.preventDefault();    // 阻止表单的默认提交

        if(!newUsername || !newEmail) {
            alert('请填写完整的用户信息');
            return;
        }

        // 创建新用户对象
        const newUser = {
            id: Date.now(),   // 简单的 ID 生成，实际项目中应由后端生成
            username: newUsername,
            email: newEmail,
            createdAt: new Date().toISOString()
        };

        // 添加到用户列表
        setUsers([...users, newUser]);

        // 清空表单
        setNewUsername('');
        setNewEmail('');
    };



    // 删除用户函数
    const handleDeleteUser = (id) => {
        if(window.confirm(`确定要删除用户 ID 为 ${id} 的用户吗?`)) {
            setUsers(users.filter(user => user.id !== id));
        }
    };

    return (
        <div>
            <h2>用户列表</h2>
            {/* 添加用户表单 */}
            <form onSubmit={handleAddUser} style={{ marginBottom: '20px'}}>
                <div>
                    <input 
                        type="text"
                        placeholder="用户名"
                        value={newUsername}
                        onChange={(e) => setNewUsername(e.target.value)}
                        style={{ marginRight: '10px', padding: '5px'}}
                    />
                    <input 
                        type="email"
                        placeholder="邮箱"
                        value={newEmail}
                        onChange={(e) => setNewEmail(e.target.value)}
                        style={{ marginRight: '10px', padding: '5px'}}
                    />
                    <button type="submit">添加新用户</button>
                </div>
            </form>
            
            <p>当前有 {users.length} 个用户</p>  {/* 动态显示用户数量 */}

            <ul>
                {users.map(user => (     // 遍历用户数组
                    <li key={user.id}>          {/* key是必需的 */}
                        {user.username} - {user.email} - {user.createdAt}
                        <button onClick={() => handleDeleteUser(user.id)} style={{ marginLeft: '10px', color: 'red' }}>删除</button>
                    </li>
                ))}
            </ul>

        </div>
    )
}

export default UserList;