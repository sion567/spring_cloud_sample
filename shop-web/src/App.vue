<template>
  <div class="app">
    <header class="header">
      <h1>SHOP 商城</h1>
      <div v-if="token" class="user-info">
        <span>{{ user.nickname }}</span>
        <button @click="logout">退出</button>
      </div>
    </header>

    <main class="main">
      <div v-if="!token" class="auth-tabs">
        <button :class="{ active: authTab === 'login' }" @click="authTab = 'login'">登录</button>
        <button :class="{ active: authTab === 'register' }" @click="authTab = 'register'">注册</button>
      </div>

      <div v-if="!token && authTab === 'login'" class="form-box">
        <h2>登录</h2>
        <form @submit.prevent="handleLogin">
          <input v-model="loginForm.username" placeholder="用户名" required>
          <input v-model="loginForm.password" type="password" placeholder="密码" required>
          <button type="submit">登录</button>
        </form>
      </div>

      <div v-if="!token && authTab === 'register'" class="form-box">
        <h2>注册</h2>
        <form @submit.prevent="handleRegister">
          <input v-model="registerForm.username" placeholder="用户名" required>
          <input v-model="registerForm.password" type="password" placeholder="密码" required>
          <input v-model="registerForm.email" type="email" placeholder="邮箱" required>
          <input v-model="registerForm.phone" placeholder="手机号" required>
          <input v-model="registerForm.nickname" placeholder="昵称">
          <button type="submit">注册</button>
        </form>
      </div>

      <div v-if="token" class="tabs">
        <button :class="{ active: tab === 'products' }" @click="tab = 'products'">商品</button>
        <button :class="{ active: tab === 'cart' }" @click="tab = 'cart'">购物车</button>
        <button :class="{ active: tab === 'orders' }" @click="tab = 'orders'">订单</button>
        <button :class="{ active: tab === 'address' }" @click="tab = 'address'">地址</button>
      </div>

      <div v-if="token && tab === 'products'" class="panel">
        <div class="product-grid">
          <div v-for="p in products" :key="p.id" class="product-card">
            <img :src="p.imageUrl || '/placeholder.png'" alt="">
            <h3>{{ p.name }}</h3>
            <p class="price">¥{{ p.price }}</p>
            <p class="stock">库存: {{ p.stock }}</p>
            <div class="product-actions">
              <input v-model.number="quantities[p.id]" type="number" min="1" :max="p.stock" value="1">
              <button @click="addToCart(p)">加入购物车</button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="token && tab === 'cart'" class="panel">
        <h2>购物车</h2>
        <div v-if="cartItems.length === 0">购物车是空的</div>
        <div v-else>
          <div v-for="item in cartItems" :key="item.id" class="cart-item">
            <span>{{ item.name }}</span>
            <span>¥{{ item.price }} × {{ item.quantity }}</span>
            <button @click="removeFromCart(item.id)">删除</button>
          </div>
          <div class="cart-total">
            <strong>总计: ¥{{ cartTotal }}</strong>
            <button @click="createOrder" :disabled="cartItems.length === 0">下单</button>
          </div>
        </div>
      </div>

      <div v-if="token && tab === 'orders'" class="panel">
        <h2>我的订单</h2>
        <div v-if="orders.length === 0">暂无订单</div>
        <div v-else>
          <div v-for="o in orders" :key="o.id" class="order-item">
            <div class="order-header">
              <span>订单号: {{ o.orderNo }}</span>
              <span :class="'status-' + o.status">{{ statusText(o.status) }}</span>
            </div>
            <div>金额: ¥{{ o.totalAmount }}</div>
            <div>时间: {{ o.createTime }}</div>
            <div v-if="o.status === 0" class="order-actions">
              <button @click="payOrder(o.id)">支付</button>
              <button @click="cancelOrder(o.id)">取消</button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="token && tab === 'address'" class="panel">
        <h2>收货地址</h2>
        <form @submit.prevent="handleAddAddress" class="address-form">
          <input v-model="addressForm.receiverName" placeholder="收货人" required>
          <input v-model="addressForm.phone" placeholder="手机号" required>
          <input v-model="addressForm.province" placeholder="省" required>
          <input v-model="addressForm.city" placeholder="市" required>
          <input v-model="addressForm.district" placeholder="区" required>
          <input v-model="addressForm.detailAddress" placeholder="详细地址" required>
          <label><input v-model="addressForm.isDefault" type="checkbox">设为默认</label>
          <button type="submit">添加地址</button>
        </form>
        <div class="address-list">
          <div v-for="a in addresses" :key="a.id" class="address-item">
            <div>{{ a.receiverName }} {{ a.phone }}</div>
            <div>{{ a.province }}{{ a.city }}{{ a.district }}{{ a.detailAddress }}</div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { userApi, productApi, orderApi } from './api.js'

const token = ref(localStorage.getItem('token') || '')
const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const authTab = ref('login')
const tab = ref('products')

const loginForm = ref({ username: '', password: '' })
const registerForm = ref({ username: '', password: '', email: '', phone: '', nickname: '' })
const addressForm = ref({ receiverName: '', phone: '', province: '', city: '', district: '', detailAddress: '', isDefault: false })

const products = ref([])
const quantities = ref({})
const cartItems = ref(JSON.parse(localStorage.getItem('cart') || '[]'))
const orders = ref([])
const addresses = ref([])

const cartTotal = computed(() => cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0))

const statusText = (status) => ['待支付', '已支付', '已发货', '已收货', '已取消'][status] || '未知'

async function handleLogin() {
  try {
    const res = await userApi.login(loginForm.value)
    token.value = res.data.token
    user.value = res.data.user
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('user', JSON.stringify(res.data.user))
    loadData()
  } catch (e) {
    alert(e.message || '登录失败')
  }
}

async function handleRegister() {
  try {
    const res = await userApi.register(registerForm.value)
    alert('注册成功，请登录')
    authTab.value = 'login'
    loginForm.value.username = registerForm.value.username
  } catch (e) {
    alert(e.message || '注册失败')
  }
}

function logout() {
  token.value = ''
  user.value = {}
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  products.value = []
  cartItems.value = []
  orders.value = []
  addresses.value = []
}

async function loadData() {
  try {
    const res = await productApi.list({ page: 1, size: 100 })
    products.value = res.data.records
    await loadOrders()
    await loadAddresses()
  } catch (e) {
    console.error(e)
  }
}

async function loadOrders() {
  if (!user.value.id) return
  try {
    const res = await orderApi.list(user.value.id, { page: 1, size: 100 })
    orders.value = res.data.records
  } catch (e) {
    console.error(e)
  }
}

async function loadAddresses() {
  if (!user.value.id) return
  try {
    const res = await userApi.getAddresses(user.value.id)
    addresses.value = res.data
  } catch (e) {
    console.error(e)
  }
}

function addToCart(product) {
  const qty = quantities.value[product.id] || 1
  const existing = cartItems.value.find(i => i.id === product.id)
  if (existing) {
    existing.quantity += qty
  } else {
    cartItems.value.push({ id: product.id, name: product.name, price: product.price, quantity: qty })
  }
  localStorage.setItem('cart', JSON.stringify(cartItems.value))
  quantities.value[product.id] = 1
}

function removeFromCart(id) {
  cartItems.value = cartItems.value.filter(i => i.id !== id)
  localStorage.setItem('cart', JSON.stringify(cartItems.value))
}

async function createOrder() {
  if (!user.value.id || cartItems.value.length === 0) return
  if (addresses.value.length === 0) {
    alert('请先添加收货地址')
    tab.value = 'address'
    return
  }
  try {
    const defaultAddr = addresses.value.find(a => a.isDefault) || addresses.value[0]
    const res = await orderApi.create({
      userId: user.value.id,
      addressId: defaultAddr.id,
      items: cartItems.value.map(i => ({ productId: i.id, quantity: i.quantity }))
    })
    cartItems.value = []
    localStorage.removeItem('cart')
    await loadOrders()
    tab.value = 'orders'
    alert('下单成功')
  } catch (e) {
    alert(e.message || '下单失败')
  }
}

async function payOrder(id) {
  try {
    await orderApi.pay(id, { payMethod: 'alipay', payNo: 'PAY' + Date.now() })
    await loadOrders()
    alert('支付成功')
  } catch (e) {
    alert(e.message || '支付失败')
  }
}

async function cancelOrder(id) {
  try {
    await orderApi.cancel(id)
    await loadOrders()
    alert('取消成功')
  } catch (e) {
    alert(e.message || '取消失败')
  }
}

async function handleAddAddress() {
  try {
    await userApi.addAddress(user.value.id, addressForm.value)
    addressForm.value = { receiverName: '', phone: '', province: '', city: '', district: '', detailAddress: '', isDefault: false }
    await loadAddresses()
    alert('添加成功')
  } catch (e) {
    alert(e.message || '添加失败')
  }
}

if (token.value) {
  loadData()
}
</script>

<style>
* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: Arial, sans-serif; background: #f5f5f5; }
.app { max-width: 1200px; margin: 0 auto; }
.header { background: #333; color: #fff; padding: 1rem; display: flex; justify-content: space-between; align-items: center; }
.header h1 { font-size: 1.5rem; }
.user-info { display: flex; gap: 1rem; align-items: center; }
.user-info button { padding: 0.3rem 0.8rem; cursor: pointer; }
.main { padding: 1rem; }
.tabs, .auth-tabs { display: flex; gap: 0.5rem; margin: 1rem 0; }
.tabs button, .auth-tabs button { padding: 0.5rem 1rem; border: 1px solid #ddd; background: #fff; cursor: pointer; }
.tabs button.active, .auth-tabs button.active { background: #333; color: #fff; }
.form-box { max-width: 400px; margin: 1rem auto; background: #fff; padding: 1.5rem; border-radius: 8px; }
.form-box form { display: flex; flex-direction: column; gap: 0.5rem; }
.form-box input { padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px; }
.form-box button { padding: 0.5rem; background: #333; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
.panel { background: #fff; padding: 1rem; border-radius: 8px; }
.product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 1rem; }
.product-card { border: 1px solid #eee; padding: 1rem; border-radius: 8px; text-align: center; }
.product-card img { max-width: 100%; height: 150px; object-fit: contain; }
.product-card h3 { margin: 0.5rem 0; font-size: 1rem; }
.price { color: #e53935; font-size: 1.2rem; font-weight: bold; }
.stock { color: #666; font-size: 0.8rem; }
.product-actions { display: flex; gap: 0.5rem; margin-top: 0.5rem; }
.product-actions input { width: 60px; padding: 0.3rem; }
.product-actions button { padding: 0.3rem 0.5rem; background: #e53935; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
.cart-item { display: flex; justify-content: space-between; padding: 0.5rem; border-bottom: 1px solid #eee; }
.cart-item button { padding: 0.2rem 0.5rem; background: #e53935; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
.cart-total { display: flex; justify-content: space-between; padding: 1rem 0; }
.cart-total button { padding: 0.5rem 1rem; background: #e53935; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
.order-item { padding: 1rem; border: 1px solid #eee; margin-bottom: 0.5rem; border-radius: 8px; }
.order-header { display: flex; justify-content: space-between; margin-bottom: 0.5rem; }
.status-0 { color: #e53935; }
.status-1 { color: #4caf50; }
.status-2 { color: #2196f3; }
.status-3 { color: #9e9e9e; }
.status-4 { color: #f44336; }
.order-actions { margin-top: 0.5rem; display: flex; gap: 0.5rem; }
.order-actions button { padding: 0.3rem 0.8rem; border: none; border-radius: 4px; cursor: pointer; }
.order-actions button:first-child { background: #4caf50; color: #fff; }
.order-actions button:last-child { background: #f44336; color: #fff; }
.address-form { display: flex; flex-direction: column; gap: 0.5rem; max-width: 400px; }
.address-form input[type="text"], .address-form input[type="email"] { padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px; }
.address-form label { display: flex; gap: 0.5rem; }
.address-form button { padding: 0.5rem; background: #333; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
.address-list { margin-top: 1rem; }
.address-item { padding: 0.5rem; border: 1px solid #eee; margin-bottom: 0.5rem; border-radius: 4px; }
</style>