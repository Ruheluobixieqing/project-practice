// API 基础配置
const API_BASE_URL = "http://localhost:8080/api";

// 等待页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    console.log('页面加载完成，JavaScript开始运行');

    // 获取页面元素
    const addUserForm = document.getElementById('add-user-form');
    const loadUsersBtn = document.getElementById('load-user-btn');
    const usersTableBody = document.getElementById('users-tbody');
    
    // 测试：在控制台输出找到的元素
    console.log('表单元素:', addUserForm);
    console.log('加载按钮:', loadUsersBtn);
    console.log('表格主体:', usersTableBody);

    // 添加事件监听器
    if (addUserForm) {
        addUserForm.addEventListener('submit', handleAddUser);
    }

    if (loadUsersBtn) {
        loadUsersBtn.addEventListener('click', handleLoadUsers);
    }
});

// 处理添加用户的函数
async function handleAddUser(event) {
    event.preventDefault();   // 防止表单默认提交行为
    console.log("用户尝试添加新用户");

    // 获取表单数据
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;

    if(!username || !email){
        alert ('请输入完成的用户信息');
        return;
    }

    console.log('准备添加用户:', {username, email});

    // 调用真实 API
    try {
        // 显示加载状态
        const submitBtn = event.target.querySelector('button[type="submit"]');
        const originalText = submitBtn.textContent;
        submitBtn.textContent = "添加中...";
        submitBtn.disabled = true;

        // 准备要发送的数据
        const userData = {
            username: username,
            email: email
        };

        // 调用后端 API
        const response = await fetch(`${API_BASE_URL}/users`,{
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
        console.log('用户添加成功', newUser);

        // 显示成功消息
        alert(`用户 "${newUser.username}" 添加成功!`);

        // 清空表单
        document.getElementById('username').value = '';
        document.getElementById('email').value = '';

        // 自动刷新用户列表
        handleLoadUsers();
    } 
    catch (error) {
        console.error('添加用户失败:', error);
        alert('添加用户失败，请检查输入信息或网络连接');
    } 
    finally {
        // 恢复按钮状态
        const submitBtn = event.target.querySelector('button[type="submit"]');
        submitBtn.textContent = '添加用户';
        submitBtn.disabled = false;
    }

}

// 处理加载用户列表的函数
async function handleLoadUsers(event) {
    console.log('开始加载用户列表...');
    
    try {
        // 显示加载状态
        const loadUserBtn = document.getElementById('load-user-btn');
        loadUserBtn.textContent = "加载中...";
        loadUserBtn.disabled = true;

        // 调用后端 API
        const response = await fetch(`${API_BASE_URL}/users`);

        if (!response.ok) {
            throw new Error(`HTTP错误: ${response.status}`);
        }

        const users = await response.json();

        // 显示用户数据表格
        displayUsers(users);
    }
    catch (error) {
        console.error('加载用户列表失败', error);
        alert('加载用户列表失败，请检查后端服务是否运行');
    }
    finally {
        // 恢复按钮状态
        const loadUsersBtn = document.getElementById('load-user-btn');
        loadUsersBtn.textContent = "加载用户列表";
        loadUsersBtn.disabled = false;
    }
}

// 显示用户数据到表格
function displayUsers(users) {
    const tableBody = document.getElementById('users-tbody');

    // 清空现有内容
    tableBody.innerHTML = '';

    if (users.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="5">暂无用户数据</td></tr>';
        return;
    }

    // 遍历用户数据，创建表格行
    users.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${formatDate(user.createdAt)}</td>
            <td>
                <button class="action-btn edit-btn" onclick="editUser(${user.id})">编辑</button>
                <button class="action-btn delete-btn" onclick="deleteUser(${user.id})">删除</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// 格式化日期列表
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString();
}

// 临时的编辑和删除函数
function editUser(id) {
    console.log('编辑用户:', id);
    alert(`编辑用户 ${id} 的功能稍后实现`);
}

function deleteUser(id) {
    console.log('删除用户:', id);
    alert(`删除用户 ${id} 的功能稍后实现`);
}