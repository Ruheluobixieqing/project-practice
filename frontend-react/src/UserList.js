import React, { useState, useEffect } from 'react';

// 配置 API 基础 URL
const API_BASE_URL = 'http://localhost:8080/api';

/*
function MyComponent(){
    // 1.组件挂载时执行一次
    useEffect(() => {
        console.log('组件已挂载');
    }, []);         // 空依赖数组，表示只在组件挂载时执行一次

    // 2.每次渲染都执行
    useEffect(() => {
        console.log('组件已重新渲染');
    });             // 无依赖数组

    // 3.当某个状态改变时执行
    useEffect(() => {
        console.log('users状态发生改变');
    }, [users]);    // users 作为依赖
}
*/

function UserList() {
    // 创建用户状态
    const [users, setUsers] = useState([
        {id: 1, username: '张三', email: 'zhangsan@example.com', createdAt: '2021-01-01'},
        {id: 2, username: '李四', email: 'lisi@example.com', createdAt: '2021-01-02'}
    ]);

    // 表单状态
    const [newUsername, setNewUsername] = useState('');
    const [newEmail, setNewEmail] = useState('');
    const [loading, setLoading] = useState(true);

    // // 模拟从服务器加载数据
    // useEffect(() => {
    //     console.log('组件挂载，开始加载数据...');

    //     //模拟网络请求延迟
    //     setTimeout(() => {
    //         console.log('数据加载完成!');
    //         setLoading(false);
    //     }, 2000);              // 时间单位为 ms
    // }, []);                    // 空依赖数组，只在组价挂载时加载一次

    // 从后端加载用户数据
    const loadUsers = async () => {
        try{
            console.log('开始从后端加载用户数据...');
            const response = await fetch(`${API_BASE_URL}/users`);

            if(!response.ok) {
                throw new Error(`HTTP错误: ${response.status}`);
            }

            const userData = await response.json();
            console.log('从后端获取到的用户数据: ', userData);

            // // 添加 1000 ms 延迟，模拟网络延迟状况
            // await new Promise(resolve => setTimeout(resolve, 1000));

            setUsers(userData);       // 更新用户状态
            setLoading(false);        // 关闭加载状态
        }
        catch (error) {
            console.error('加载用户失败:',error);
            alert('加载用户数据失败，请检查后端服务器是否运行');
            setLoading(false);
        }
    }

    // 组件挂载时调用
    useEffect(() =>{
        loadUsers();
    },[]);

    // 添加用户函数
    const handleAddUser = async (e) => {
        e.preventDefault();    // 阻止表单的默认提交

        if(!newUsername || !newEmail) {
            alert('请填写完整的用户信息');
            return;
        }

        // // 创建新用户对象
        // const newUser = {
        //     id: Date.now(),   // 简单的 ID 生成，实际项目中应由后端生成
        //     username: newUsername,
        //     email: newEmail,
        //     createdAt: new Date().toISOString()
        // };

        // // 添加到用户列表
        // setUsers([...users, newUser]);

        // // 清空表单
        // setNewUsername('');
        // setNewEmail('');

        try{
            // 准备发送给后端的数据
            const userData = {
                username: newUsername,
                email: newEmail
            };

            console.log('发送新用户数据至后端:', userData);

            // 调用后端 API
            const response = await fetch(`${API_BASE_URL}/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });

            if (!response.ok) {
                throw new Error(`HTTP错误: ${response.status}`);
            }

            const newUser = await response.json();
            console.log('后端返回的新用户:', newUser);

            // 更新前端状态
            setUsers([...users, newUser]);

            // 清空表单
            setNewUsername('');
            setNewEmail('');

            alert(`用户 "${newUser.username}" 添加成功！`);
        }
        catch (error) {
            console.error('添加用户失败:', error);
            alert('添加用户失败,请检查网络连接或后端服务');
        }
    };

    // 删除用户函数
    const handleDeleteUser = async (id) => {
        if(!window.confirm(`确定要删除用户 ID 为 ${id} 的用户吗?`)) {
            return;
        }

        try{
            console.log('删除用户 ID: ', id);

            // 调用后端 API
            const response = await fetch(`${API_BASE_URL}/users/${id}`,{
                method: 'DELETE'
            });

            if(!response.ok){
                throw new Error(`HTTP错误: ${response.status}`);
            }

            console.log('用户删除成功!');

            // 更新前端状态
            setUsers(users.filter(user => user.id !== id));
            alert('用户删除成功!');
        }
        catch (error) {
            console.error('删除用户失败:', error);
            alert('删除用户失败，请检查网络连接或后端服务');
        }
    };

    if (loading) {
        return (
            <div>
                <h2>用户列表</h2>
                <p>正在加载用户数据...</p>
            </div>
        );
    }

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