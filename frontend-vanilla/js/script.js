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

    // 增强用户验证
    if (!username || !email) {
        alert('请输入完整的用户信息');
        return;
    }
    
    // 验证邮箱格式
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('请输入有效的邮箱地址');
        return;
    }
    
    // 验证用户名长度
    if (username.length < 2) {
        alert('用户名至少需要2个字符');
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
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align: center; padding: 2rem; color: #666;">
                    <div>
                        <p style="margin-bottom: 1rem;">暂无用户数据</p>
                        <button onclick="handleLoadUsers()" style="padding: 0.5rem 1rem;">刷新数据</button>
                    </div>
                </td>
            </tr>
        `;
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

async function editUser(id) {
    console.log('编辑用户:', id);

    try {
        // 先获取用户当前信息
        const response = await fetch(`${API_BASE_URL}/users/${id}`);

        if (!response.ok) {
            throw new Error(`HTTP错误: ${response.status}`);
        }
    
        const user = await response.json();
        console.log('获取到用户信息:', user);

        // 使用 prompt 获取新的用户信息（简单方式）
        const newUsername = prompt('请输入新的用户名:', user.username);
        if(newUsername === null) return;  // 用户取消

        const newEmail = prompt('请输入新的邮箱:', user.email);
        if(newEmail === null) return;  // 用户取消

        // 验证输入
        if(!newUsername.trim() || !newEmail.trim()) {
            alert('用户名和邮箱不能为空');
            return;
        }
        // 准备更新数据
        const updateData = {
            username: newUsername.trim(),
            email: newEmail.trim()
        };
        
        // 调用更新API
        const updateResponse = await fetch(`${API_BASE_URL}/users/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updateData)
        });
        
        if (!updateResponse.ok) {
            throw new Error(`HTTP错误: ${updateResponse.status}`);
        }
        
        const updatedUser = await updateResponse.json();
        console.log('用户更新成功:', updatedUser);
        alert(`用户信息更新成功！`);
        
        // 刷新用户列表
        handleLoadUsers();
        
    } 
    catch (error) {
        console.error('编辑用户失败:', error);
        alert('编辑用户失败，请稍后重试');
    }
}

async function deleteUser(id) {
    console.log('准备删除用户:', id);
    
    // 确认删除操作
    const confirmDelete = confirm(`确定要删除 ID 为 ${id} 的用户吗？此操作不可撤销！`);

    if (!confirmDelete) {
        console.log("用户取消了删除操作");
        return;
    }

    try {
        // 调用后端 API 删除用户
        const response = await fetch(`${API_BASE_URL}/users/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error(`HTTP错误: ${response.status}`);
        }

        console.log(`用户 ${id} 删除成功!`);
        alert(`用户删除成功!`);

        // 删除用户列表
        handleLoadUsers();
    }
    catch (error) {
        console.error('删除用户失败：', error);
        alert('删除用户失败，请稍后再试');
    }
}