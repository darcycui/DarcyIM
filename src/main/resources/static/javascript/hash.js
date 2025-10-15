
async function sha256(message) {
    // 将字符串转换为 ArrayBuffer
    const msgBuffer = new TextEncoder().encode(message);
    // 使用 crypto.subtle 计算哈希
    const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
    // 将哈希结果转换为十六进制字符串
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
}