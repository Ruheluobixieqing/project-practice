import React, { useState } from 'react';

function UserList() {
    // 创建用户状态
    const [users, setUsers] = useState([
        {id: 1, username: '张三', email: 'zhangsan@example.com', createdAt: '2021-01-01'},
        {id: 2, username: '李四', email: 'lisi@example.com', createdAt: '2021-01-02'}
    ]);

    // 删除用户函数
    const handleDeleteUser = (id) => {
        if(window.confirm(`确定要删除用户 ID 为 ${id} 的用户吗?`)) {
            setUsers(users.filter(user => user.id !== id));
        }
    };

    return (
        <div>
            <h2>用户列表</h2>
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