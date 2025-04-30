includelib msvcrt.lib
includelib ucrt.lib
includelib legacy_stdio_definitions.lib
includelib ws2_32.lib
includelib kernel32.lib

EXTERN printf:PROC
EXTERN WSAStartup:PROC
EXTERN socket:PROC
EXTERN connect:PROC
EXTERN send:PROC
EXTERN WSACleanup:PROC
EXTERN closesocket:PROC
EXTERN recv:PROC
EXTERN GetStdHandle:PROC
EXTERN ReadConsoleA:PROC

STD_INPUT_HANDLE EQU -10
AF_INET      EQU 2
SOCK_STREAM  EQU 1
IPPROTO_TCP  EQU 6

WSADATA STRUCT
    wVersion        WORD ?
    wHighVersion    WORD ?
    szDescription   BYTE 257 dup (?)
    szSystenStatus  BYTE 129 dup (?)
    iMaxSockets     WORD ?
    iMaxUdpDg       WORD ?
    lpVendorInfo    QWORD ?
WSADATA ENDS

SOCKADDR_IN STRUCT
    sin_family  WORD ?
    sin_port    WORD ?
    sin_addr    DWORD ?
    sin_zero    BYTE 8 dup (?)
SOCKADDR_IN ENDS

.data
wsa WSADATA <>
sockaddr SOCKADDR_IN <>

successMsg db "WinSock on!", 10, 0
socketSuccess db "Socket on!", 10, 0
inputPrompt db " ", 0
receivedFmt db "%.*s", 10, 0

buffer db 256 dup (?)
inputBuffer db 256 dup (?)
bytesRead dd ?

.code
main PROC
    sub rsp, 28h

    mov rcx, STD_INPUT_HANDLE
    call GetStdHandle
    mov r12, rax

    mov rcx, 0202h
    lea rdx, wsa
    call WSAStartup

    mov rcx, AF_INET
    mov rdx, SOCK_STREAM
    mov r8, IPPROTO_TCP
    call socket
    mov rbx, rax


    mov word ptr [sockaddr.sin_family], AF_INET
    mov word ptr [sockaddr.sin_port], 3930h             
    mov dword ptr [sockaddr.sin_addr], 8035F51Ah         

    mov rcx, rbx
    lea rdx, sockaddr
    mov r8d, 16
    call connect

main_loop:

    lea rcx, inputPrompt
    call printf

    mov rcx, r12
    lea rdx, inputBuffer
    mov r8d, 256
    lea r9, bytesRead
    sub rsp, 20h
    call ReadConsoleA
    add rsp, 20h

    mov rax, 0
    movzx rcx, byte ptr [inputBuffer]
clean_loop:
    cmp byte ptr [inputBuffer + rcx], 13
    je zero_label
    cmp byte ptr [inputBuffer + rcx], 10
    je zero_label
    cmp byte ptr [inputBuffer + rcx], 0
    je fim_label
    inc rcx
    jmp clean_loop
zero_label:
    mov byte ptr [inputBuffer + rcx], 0

    mov eax, [bytesRead]
    sub eax, 2
    mov [bytesRead], eax

fim_label:

    mov rcx, rbx
    lea rdx, inputBuffer
    mov r8d, [bytesRead]
    xor r9d, r9d
    call send

    mov rsi, offset buffer
    mov rdi, rsi
    mov ecx, 256 / 8
    xor eax, eax
    cld
    rep stosq

    mov rcx, rbx
    lea rdx, buffer
    mov r8d, 256
    xor r9d, r9d
    call recv

    cmp rax, 0
    jle fechar_socket

    lea rcx, receivedFmt
    mov edx, eax
    lea r8, buffer
    call printf

    jmp main_loop

fechar_socket:
    mov rcx, rbx
    call closesocket

    lea rcx, socketSuccess
    call printf

    lea rcx, successMsg
    call printf

    call WSACleanup

    add rsp, 28h
    ret
main ENDP
END