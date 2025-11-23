import type React from "react"
import { useRef } from "react"
import { Canvas, useFrame } from "@react-three/fiber"
import { Text3D, Center, Float, Environment } from "@react-three/drei"
import type { Mesh } from "three"

const Coin = (props: any) => {
  const meshRef = useRef<Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.y += 0.01
      meshRef.current.rotation.z = Math.sin(state.clock.getElapsedTime()) * 0.1
    }
  })

  return (
    <group {...props}>
      <Float speed={2} rotationIntensity={1} floatIntensity={1}>
        <mesh ref={meshRef} rotation={[Math.PI / 2, 0, 0]}>
          <cylinderGeometry args={[2.5, 2.5, 0.3, 32]} />
          <meshStandardMaterial
            color="#FFD700"
            metalness={0.8}
            roughness={0.2}
            emissive="#FFAA00"
            emissiveIntensity={0.2}
          />
        </mesh>

        {/* Dollar Sign */}
        <Center position={[0, 0, 0.16]}>
          <Text3D
            size={2}
            height={0.1}
            curveSegments={12}
            bevelEnabled
            bevelThickness={0.02}
            bevelSize={0.02}
            bevelOffset={0}
            bevelSegments={5} font={"/fonts/Geist_Bold.json"}       >
            $
            <meshStandardMaterial color="#FFF" metalness={0.5} roughness={0.5} />
          </Text3D>
        </Center>
        <Center position={[0, 0, -0.16]} rotation={[0, Math.PI, 0]}>
          <Text3D
            size={2}
            height={0.1}
            curveSegments={12}
            bevelEnabled
            bevelThickness={0.02}
            bevelSize={0.02}
            bevelOffset={0}
            bevelSegments={5} font={"/fonts/Geist_Bold.json"}            >
            $
            <meshStandardMaterial color="#FFF" metalness={0.5} roughness={0.5} />
          </Text3D>
        </Center>
      </Float>
    </group>
  )
}

export const FloatingMoneyScene: React.FC = () => {
  return (
    <div className="w-full h-full min-h-[400px]">
      <Canvas camera={{ position: [0, 0, 8], fov: 45 }}>
        <ambientLight intensity={0.5} />
        <spotLight position={[10, 10, 10]} angle={0.15} penumbra={1} intensity={1} />
        <pointLight position={[-10, -10, -10]} intensity={1} />
        <Coin position={[0, 0, 0]} />
        <Environment preset="city" />
      </Canvas>
    </div>
  )
}
